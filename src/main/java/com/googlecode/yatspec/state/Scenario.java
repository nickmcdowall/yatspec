package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Scenario {
    private TestState testState = new TestState();
    private final String name;
    private final JavaSource specification;
    private Throwable exception;
    private boolean wasRun = false;
    private SvgWrapper diagram;

    public Scenario(String name, JavaSource specification) {
        this.name = name;
        this.specification = specification;
    }

    public String getName() {
        return name;
    }

    public void copyTestState(TestState testState) {
        this.testState = new TestState(testState);
    }

    public SvgWrapper getDiagram() {
        return testState.getDiagram();
    }

    public Map<LogKey, Object> getLogs() {
        Map<LogKey, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : getCapturedInputAndOutputs().entrySet()) {
            result.put(new LogKey(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public Map<String, Object> getCapturedInputAndOutputs() {
        return testState.getCapturedTypes();
    }

    public Map<String, Object> getInterestingGivens() {
        return testState.getInterestingTypes();
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean hasFailed() {
        return exception != null;
    }

    public JavaSource getSpecification() {
        return specification;
    }

    public boolean wasRun() {
        return wasRun;
    }

    public void hasRun(boolean value) {
        wasRun = value;
    }

    public String getMessage() {
        String result = "Test not run";
        if (wasRun()) {
            result = "Test passed";
        }
        if (hasFailed()) {
            result = getException().toString();
        }
        return result;
    }

    public Status getStatus() {
        if (hasFailed()) {
            return Status.Failed;
        }
        if (wasRun()) {
            return Status.Passed;
        }
        return Status.NotRun;
    }

    public String getUid() {
        return name.hashCode() + "_" + specification.hashCode();
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
