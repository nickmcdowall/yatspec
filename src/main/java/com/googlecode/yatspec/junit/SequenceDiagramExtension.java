package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.util.Optional;

public class SequenceDiagramExtension implements TestInstancePostProcessor, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    Optional<TestState> testState;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) {
        testState = Optional.of(testInstance)
                .filter(TestState.class::isInstance)
                .map(TestState.class::cast);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        testState.ifPresent(testState -> {
            var messages = new ByNamingConventionMessageProducer().messages(testState.capturedInputAndOutputs);
            testState.capturedInputAndOutputs.add(
                    "Sequence Diagram", sequenceDiagramGenerator.generateSequenceDiagram(messages));
        });
    }
}
