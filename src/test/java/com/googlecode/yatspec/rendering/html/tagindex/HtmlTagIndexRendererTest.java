package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlFileRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.rootDirectoryFor;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlTagIndexRendererTest {

    private final HtmlTagIndexRenderer renderer = new HtmlTagIndexRenderer();
    private final File tempDirectory;
    private final InputStream expectedTagIndex;

    HtmlTagIndexRendererTest() throws IOException {
        tempDirectory = Files.createTempDirectory("htmltagindexrenderertest").toFile();
        expectedTagIndex = loadResource("/expected/TagIndexRenderingTest.html");
        renderer.index.reset();
    }

    @Notes("I am an example test method with a tag #tag-name")
    @Test
    void renderATagIndex() throws Exception {
        renderer.complete(tempDirectory, aStubbedResult("package.name"));

        assertThat(outputFile(tempDirectory, "tag-index.html")).hasSameContentAs(expectedTagIndex);
    }

    private InputStream loadResource(String name) {
        return getClass().getResourceAsStream(name);
    }

    private static FileInputStream outputFile(File outputDirectory, String name) throws FileNotFoundException {
        return new FileInputStream(new File(outputDirectory, name));
    }

    private Result aStubbedResult(final String packageName) {
        return new Result() {
            private final List<TestMethod> testMethods = stubbedMethods();

            @Override
            public List<TestMethod> getTestMethods() {
                return testMethods;
            }

            @Override
            public Scenario getScenario(String name) {
                return null;
            }

            @Override
            public String getName() {
                return "the name of the test";
            }

            @Override
            public String getPackageName() {
                return packageName;
            }

            @Override
            public String getRootDirectory() {
                return rootDirectoryFor(getClass());
            }

            @Override
            public String getHtmlFileRelativePath() {
                return htmlFileRelativePath(getClass());
            }

            private List<TestMethod> stubbedMethods() {
                return stream(getClass().getDeclaredMethods())
                        .filter(method -> stream(method.getDeclaredAnnotations())
                                .anyMatch(annotation -> annotation.annotationType().equals(Test.class)))
                        .map(this::aTestMethodWrapper)
                        .collect(toList());
            }

            @Notes("I am an example test method with #tag-name1 and #tag-name2")
            @Test
            void exampleTestMethodA() {
            }

            @Notes("I am another example test method with #tag-name3")
            @Test
            void exampleTestMethodB() {
            }

            @Notes("I am another example test method with #tag-name2")
            @Test
            void exampleTestMethodC() {
            }

            private TestMethod aTestMethodWrapper(Method method) {
                return new TestMethod(
                        method.getDeclaringClass(),
                        method,
                        method.getName(),
                        new JavaSource(""),
                        new ScenarioTable()
                );
            }
        };
    }
}
