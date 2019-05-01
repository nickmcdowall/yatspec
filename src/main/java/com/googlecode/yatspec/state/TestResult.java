package com.googlecode.yatspec.state;


import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.parsing.Text;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.googlecode.yatspec.junit.YatspecAnnotation.methods.yatspecAnnotations;
import static java.util.Arrays.asList;

@SuppressWarnings({"unused"})
public class TestResult implements Result {
    private final Class<?> klass;
    private List<TestMethod> testMethods;

    public TestResult(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public Class<?> getTestClass() {
        return klass;
    }

    @Override
    public List<TestMethod> getTestMethods() throws Exception {
        if (testMethods == null) {
            testMethods = TestParser.parseTestMethods(klass);
        }
        return testMethods;
    }

    @Override
    public Scenario getScenario(String name) throws Exception {
        final Scenario testScenario = findScenario(name);
        testScenario.hasRun(true);
        return testScenario;
    }

    @Override
    public String getName() {
        String className = getTestClass().getSimpleName();
        if (className.endsWith("Test")) {
            className = removeTestFrom(className);
        }
        return Text.wordify(className);
    }

    @Override
    public String getPackageName() {
        return getTestClass().getPackage().getName();
    }

    private static String removeTestFrom(String className) {
        final int index = className.lastIndexOf("Test");
        return className.substring(0, index);
    }

    private Scenario findScenario(final String name) throws Exception {
        return getTestMethods().stream()
                .filter(testMethod -> testMethod.hasScenario(name))
                .findFirst().get()
                .getScenario(name);
    }

    public List<Annotation> getAnnotations() {
        return yatspecAnnotations(asList(getTestClass().getAnnotations()));
    }
}
