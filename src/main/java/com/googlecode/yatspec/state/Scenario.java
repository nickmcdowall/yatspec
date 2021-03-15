package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.TestText;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class Scenario {
    private final String name;
    private final TestText testText;

    private TestState testState = new TestState();
    private boolean wasRun = false;
    private FailureMessage failureMessage;

    public void copyTestState(TestState testState) {
        this.testState = new TestState(testState);
    }

    public SvgWrapper getDiagram() {
        return testState.getDiagram();
    }

    @SuppressWarnings("unused") //used by template
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

    public boolean hasFailed() {
        return Optional.ofNullable(failureMessage).isPresent();
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
            result = failureMessage.getValue();
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
        return name.hashCode() + "_" + testText.hashCode();
    }
}
