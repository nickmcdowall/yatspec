package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class SequenceDiagramExtension extends SpecListener implements TestInstancePostProcessor, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private SequenceDiagramGenerator sequenceDiagramGenerator;
    private Optional<TestState> interactions = Optional.empty();
    private List<Participant> participants = emptyList();


    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) {
        interactions = getOptionalTestState(testInstance);
        if (testInstance instanceof WithParticipants) {
            participants = Optional.ofNullable(((WithParticipants) testInstance).participants()).orElse(emptyList());
        }
    }


    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        interactions.ifPresent(interactions ->
                interactions.setDiagram(sequenceDiagramGenerator.generateSequenceDiagram(interactions.sequenceMessages(), participants)));
    }

}
