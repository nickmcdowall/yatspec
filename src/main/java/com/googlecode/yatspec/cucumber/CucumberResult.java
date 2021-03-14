package com.googlecode.yatspec.cucumber;

import com.googlecode.yatspec.parsing.TestText;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStep;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlFileRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.rootDirectoryFor;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

class CucumberResult implements Result {
    private final Class<?> klass;
    private final List<TestMethod> testMethods = new ArrayList<>();

    public CucumberResult(Class<?> klass) {
        this.klass = klass;
    }

    private TestText testTextFrom(List<TestStep> steps) {
        return new TestText(steps.stream()
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
    public Scenario getScenario(String name) {
        final Scenario testScenario = findScenario(name);
        testScenario.hasRun(true);
        return testScenario;
    }

    private Scenario findScenario(final String name) {
        return testMethods.stream()
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

    @Override
    public String getRootDirectory() {
        return rootDirectoryFor(klass);
    }

    @Override
    public String getHtmlFileRelativePath() {
        return htmlFileRelativePath(klass);
    }

    public void add(String name, List<TestStep> steps) {
        testMethods.add(new TestMethod(klass, name, testTextFrom(steps), new ScenarioTable(), emptyList()));
    }
}
