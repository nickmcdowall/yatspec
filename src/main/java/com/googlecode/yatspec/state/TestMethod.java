package com.googlecode.yatspec.state;

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

import static com.googlecode.yatspec.junit.YatspecAnnotation.methods.yatspecAnnotations;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class TestMethod {
    private final Class<?> testClass;
    private final Method method;
    private final String methodName;
    private final ScenarioTable scenarioTable;
    private final JavaSource specification;
    private final Map<String, Scenario> scenarioResults = new LinkedHashMap<>();

    public TestMethod(Class<?> testClass, Method method, String methodName, JavaSource methodBody, ScenarioTable scenarioTable) {
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
                final List<String> headers = scenarioTable.getHeaders().stream()
                        .map(ScenarioTableHeader::value)
                        .collect(toList());
                List<String> processedValues = processValues(row, headers);
                Scenario scenario = new Scenario(name, specification.replace(headers, processedValues));
                scenarioResults.put(name, scenario);
            }
        }
    }

    protected List<String> processValues(List<String> values, List<String> headers) {
        if (hasVarargs(values, headers)) {
            List<String> normalArgs = values.subList(0, headers.size() - 1);
            List<String> varArgs = values.subList(headers.size() - 1, values.size());
            String varArgsJoined = varArgs.stream().collect(joining(", ", "[", "]"));
            return concatenate(normalArgs, List.of(varArgsJoined));
        }
        if (missingArgs(values, headers)) {
            List<String> emptyArgs = nCopies(headers.size() - values.size(), "[]");
            return concatenate(values, emptyArgs);
        }
        return values;
    }

    private List<String> concatenate(List<String> a, List<String> b) {
        return concat(a.stream(), b.stream()).collect(toList());
    }

    private boolean missingArgs(List<String> values, List<String> headers) {
        return headers.size() > values.size();
    }

    private boolean hasVarargs(List<String> values, List<String> headers) {
        return values.size() > headers.size();
    }

    public String getName() {
        return methodName;
    }

    public String getDisplayName() {
        return Text.wordify(methodName);
    }

    public Status getStatus() {
        List<Status> statuses = getScenarios().stream()
                .map(Scenario::getStatus)
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


    @SuppressWarnings("unused") // used via template
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
        return yatspecAnnotations(asList(method.getAnnotations()));
    }

    public String getUid() {
        return Integer.toString(methodName.hashCode());
    }

    public String getPackageName() {
        return testClass.getPackage().getName();
    }

    public Class<?> getTestClass() {
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
