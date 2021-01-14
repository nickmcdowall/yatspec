package com.googlecode.yatspec.cucumber;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStep;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

class CucumberResult implements Result {
    private final Class<?> klass;
    private final Method dummyMethod = this.getClass().getMethods()[0]; //Only used to get at the annotations
    private final List<TestMethod> testMethods = new ArrayList<>();

    public CucumberResult(Class<?> klass) {
        this.klass = klass;
    }

    private JavaSource stepsToJavaSource(List<TestStep> steps) {
        return new JavaSource(steps.stream()
                .filter(PickleStepTestStep.class::isInstance)
                .map(PickleStepTestStep.class::cast)
                .map(v -> v.getStep().getKeyword() + " " + v.getStep().getText())
                .collect(joining("\n")));
    }

    @Override
    public List<TestMethod> getTestMethods() {
        return testMethods;
    }

    @Override
    public Class<?> getTestClass() {
        return klass;
    }

    @Override
    public Scenario getScenario(String name) {
        final Scenario testScenario = findScenario(name);
        testScenario.hasRun(true);
        return testScenario;
    }

    private Scenario findScenario(final String name) {
        return getTestMethods().stream()
                .filter(testMethod -> testMethod.hasScenario(name))
                .findFirst().get()
                .getScenario(name);
    }

    @Override
    public String getName() {
        return Text.wordify(klass.getSimpleName());
    }

    @Override
    public String getPackageName() {
        return klass.getPackageName();
    }

    public void add(String name, List<TestStep> steps) {
        JavaSource methodBody = stepsToJavaSource(steps);
        testMethods.add(new TestMethod(klass, dummyMethod, name, methodBody, new ScenarioTable()));
    }
}
