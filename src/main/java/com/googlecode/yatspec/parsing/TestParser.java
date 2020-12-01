package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.System.getProperty;
import static java.nio.file.Files.walk;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class TestParser {

    public static List<TestMethod> parseTestMethods(Class<?> aClass) {
        return extractTestMethods(aClass, extractJavaMethods(aClass));
    }

    private static List<TestMethod> extractTestMethods(Class<?> aClass, List<Method> methods) {
        final Optional<JavaClass> javaClass = getJavaClass(aClass);
        if (javaClass.isEmpty()) {
            return List.of();
        }

        List<JavaMethod> targetJavaMethods = extractJavaMethods(javaClass.get(),
                annotatedWith(Test.class).or(annotatedWith(ParameterizedTest.class)));

        Map<String, List<Method>> realMethodsByName = methods.stream()
                .collect(groupingBy(Method::getName));

        List<TestMethod> testMethods = new ArrayList<>();
        TestMethodExtractor extractor = new TestMethodExtractor();

        // TODO Handle overloaded methods (i.e. multiple matches for same name)
        for (JavaMethod javaMethod : targetJavaMethods) {
            List<Method> realMethods = realMethodsByName.get(javaMethod.getName());
            testMethods.add(extractor.toTestMethod(aClass, javaMethod, realMethods.get(0)));
        }

        List<TestMethod> parentTestMethods = extractTestMethods(aClass.getSuperclass(), methods);

        return concat(testMethods.stream(), parentTestMethods.stream())
                .collect(toList());
    }

    private static Optional<JavaClass> getJavaClass(final Class<?> aClass) {
        Optional<URL> url = getJavaSourceUrlFromClassPath(aClass);
        url = url.isPresent() ? url : getJavaSourceUrlFromFileSystem(aClass);
        return url.map(asAJavaClass(aClass));
    }

    private static Function<URL, JavaClass> asAJavaClass(final Class<?> aClass) {
        return url -> {
            JavaDocBuilder builder = new JavaDocBuilder();
            try {
                builder.addSource(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return builder.getClassByName(aClass.getName());
        };
    }

    private static List<Method> extractJavaMethods(Class<?> aClass) {
        Stream<Method> declaredMethods = stream(aClass.getDeclaredMethods());
        Stream<Method> allPublicMethods = stream(aClass.getMethods());

        return concat(declaredMethods, allPublicMethods)
                .filter(hasAnnotationOfType(Test.class)
                        .or(hasAnnotationOfType(ParameterizedTest.class)))
                .distinct()
                .collect(toList());
    }


    private static List<JavaMethod> extractJavaMethods(JavaClass javaClass, Predicate<Annotation> predicate) {
        return stream(javaClass.getMethods())
                .filter(method -> annotationsOf(method).stream()
                        .anyMatch(predicate)
                ).collect(toList());
    }

    private static Optional<URL> getJavaSourceUrlFromClassPath(Class<?> aClass) {
        return isObject(aClass) ? Optional.empty() : Optional.ofNullable(getResourceUrl(aClass));
    }

    private static URL getResourceUrl(Class<?> aClass) {
        return aClass.getClassLoader().getResource(FilesUtil.toJavaResourcePath(aClass));
    }

    private static Optional<URL> getJavaSourceUrlFromFileSystem(Class<?> aClass) {
        return isObject(aClass) ? Optional.empty() : findSourceUrlFor(aClass);
    }

    private static Optional<URL> findSourceUrlFor(Class<?> aClass) {
        return findFile(workingDirectory(), FilesUtil.toJavaPath(aClass))
                .map(TestParser::urlFor);
    }

    private static Optional<File> findFile(String searchDirectory, String name) {
        try (Stream<Path> walkStream = walk(Paths.get(searchDirectory))) {
            return walkStream.filter(isAFile().and(isASourceFile()).and(hasName(name)))
                    .map(Path::toFile)
                    .findFirst();
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static Predicate<? super Path> hasName(String name) {
        return path -> path.toFile().getAbsolutePath().endsWith(name);
    }

    private static Predicate<? super Path> isASourceFile() {
        return path -> path.toFile().getName().endsWith(".java");
    }

    private static Predicate<Path> isAFile() {
        return path -> path.toFile().isFile();
    }

    private static String workingDirectory() {
        return getProperty("user.dir");
    }

    private static URL urlFor(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isObject(Class<?> aClass) {
        return aClass.equals(Object.class);
    }

    private static Predicate<Annotation> annotatedWith(final Class<?> aType) {
        return annotation -> nameOf(annotation).equalsIgnoreCase((aType.getName()));
    }

    private static String nameOf(Annotation annotation) {
        return annotation.getType().getFullyQualifiedName();
    }

    private static List<Annotation> annotationsOf(JavaMethod javaMethod) {
        return asList(javaMethod.getAnnotations());
    }

    private static Predicate<Method> hasAnnotationOfType(final Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return method -> null != method.getAnnotation(annotationClass);
    }
}
