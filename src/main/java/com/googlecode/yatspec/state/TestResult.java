package com.googlecode.yatspec.state;


import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.parsing.Text;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.googlecode.yatspec.junit.YatspecAnnotation.methods.yatspecAnnotations;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlFileRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.rootDirectoryFor;
import static java.util.Arrays.asList;

public class TestResult implements Result {
    private final Class<?> klass;
    private final List<TestMethod> testMethods;

    public TestResult(Class<?> klass) {
        this.klass = klass;
        this.testMethods = TestParser.parseTestMethods(klass);
    }

    @Override
    public List<TestMethod> getTestMethods() {
        return testMethods;
    }

    @Override
    public Scenario getScenario(String name) {
        final Scenario testScenario = findScenario(name);
        testScenario.hasRun(true);
        return testScenario;
    }

    @Override
    public String getName() {
        String className = klass.getSimpleName();
        if (className.endsWith("Test")) {
            className = removeTestFrom(className);
        }
        return Text.wordify(className);
    }

    @Override
    public String getPackageName() {
        return klass.getPackage().getName();
    }

    private static String removeTestFrom(String className) {
        final int index = className.lastIndexOf("Test");
        return className.substring(0, index);
    }

    private Scenario findScenario(final String name) {
        return testMethods.stream()
                .filter(testMethod -> testMethod.hasScenario(name))
                .findFirst().get()
                .getScenario(name);
    }

    @Override
    public List<Annotation> getAnnotations() {
        return yatspecAnnotations(asList(klass.getAnnotations()));
    }

    @Override
    public String getRootDirectory() {
        return rootDirectoryFor(klass);
    }

    @Override
    public String getHtmlFileRelativePath() {
        return htmlFileRelativePath(klass);
    }
}
