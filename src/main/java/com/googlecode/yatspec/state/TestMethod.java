package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.ScenarioNameRendererFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.junit.YatspecAnnotation.methods.yatspecAnnotations;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;

@SuppressWarnings({"unused"})
public class TestMethod {
    private final Class testClass;
    private final Method method;
    private final String methodName;
    private final ScenarioTable scenarioTable;
    private final JavaSource specification;
    private final Map<String, Scenario> scenarioResults = new LinkedHashMap<>();

    public TestMethod(Class testClass, Method method, String methodName, JavaSource methodBody, ScenarioTable scenarioTable) {
        this.testClass = testClass;
        this.method = method;
        this.methodName = methodName;
        this.scenarioTable = scenarioTable;
        this.specification = methodBody;
        buildUpScenarios();
    }

    private void buildUpScenarios() {
        if (scenarioTable.isEmpty()) {
            scenarioResults.put(methodName, new Scenario("", specification));
        } else {
            for (List<String> row : scenarioTable.getRows()) {
                ScenarioName scenarioName = new ScenarioName(methodName, row);
                String name = ScenarioNameRendererFactory.renderer().render(scenarioName);

                final List<String> oldValues = scenarioTable.getHeaders().stream()
                        .map(ScenarioTableHeader::value)
                        .collect(toList());

                Scenario scenario = new Scenario(name, specification.replace(oldValues, createPossiblyVarargValueFrom(row, oldValues)));
                scenarioResults.put(name, scenario);
            }
        }
    }

    private List<String> createPossiblyVarargValueFrom(List<String> newValues, List<String> oldValues) {
        Sequence<String> actualValues = sequence(newValues);
        if (oldValues.size() > newValues.size()) {
            actualValues = sequence(newValues).join(sequence("[]").cycle()).take(oldValues.size());
        } else if (newValues.size() > oldValues.size()) {
            actualValues = actualValues.take(oldValues.size() - 1).append(actualValues.drop(oldValues.size() - 1).toString("[", ", ", "]"));
        }
        return actualValues.toList();
    }

    private static <T> Callable1<? super Value<T>, T> value(Class<T> aClass) {
        return (Callable1<Value<T>, T>) instance -> instance.value();
    }

    public String getName() {
        return methodName;
    }

    public String getDisplayName() {
        return Text.wordify(methodName);
    }

    public String getDisplayLinkName() {
        return getDisplayName().replace(' ', '_');
    }

    public Status getStatus() {
        List<Status> statuses = getScenarios().stream()
                .map(scenario -> scenario.getStatus())
                .collect(toList());
        return calculateStatus(statuses);
    }

    private static Status calculateStatus(final List<Status> statuses) {
        if (statuses.contains(Status.Failed)) {
            return Status.Failed;
        }
        if (statuses.contains(Status.NotRun)) {
            return Status.NotRun;
        }
        return Status.Passed;
    }

    public JavaSource getSpecification() {
        return specification;
    }

    @Override
    public String toString() {
        return getName() + lineSeparator() + getSpecification();
    }


    public ScenarioTable getScenarioTable() {
        return scenarioTable;
    }

    public Scenario getScenario(String fullName) {
        return scenarioResults.get(fullName);
    }

    public List<Scenario> getScenarios() {
        return new ArrayList<>(scenarioResults.values());
    }

    public boolean hasScenario(String name) {
        return scenarioResults.get(name) != null;
    }

    public List<Annotation> getAnnotations() {
        return yatspecAnnotations(sequence(method.getAnnotations()));
    }

    public String getUid() {
        return Integer.toString(methodName.hashCode());
    }

    public String getPackageName() {
        return testClass.getPackage().getName();
    }

    public Class getTestClass() {
        return testClass;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
