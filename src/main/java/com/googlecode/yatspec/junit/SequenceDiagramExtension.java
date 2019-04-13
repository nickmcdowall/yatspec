package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.util.Optional;

public class SequenceDiagramExtension implements TestInstancePostProcessor, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final String SEQUENCE_DIAGRAM = "Sequence Diagram";
    private SequenceDiagramGenerator sequenceDiagramGenerator;
    private Optional<TestState> interactions = Optional.empty();


    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) {
        if (testInstance instanceof WithTestState) {
            interactions = Optional.ofNullable(((WithTestState) testInstance).testState());
        }
    }


    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        interactions.ifPresent(interactions ->
                interactions.log(SEQUENCE_DIAGRAM, sequenceDiagramGenerator.generateSequenceDiagram(interactions.sequenceMessages())));
    }

}
