package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class PlantUmlMarkupGeneratorTest {

    @Test
    void generatesPlantUmlMarkup() {
        String markup = PlantUmlMarkupGenerator.generateMarkup(List.of(new SequenceDiagramMessage("Bob", "Alice", "How are you Alice?", "message_id")), emptyList());
        assertThat(markup).isEqualToIgnoringWhitespace("@startuml Bob ->> Alice:<text class=sequence_diagram_clickable sequence_diagram_message_id=message_id>How are you Alice?</text> @enduml");
    }

    @Test
    void doesGroups() {
        CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();
        capturedInputAndOutputs.add("(hello) a message from here to there", "message body");
        String markup = PlantUmlMarkupGenerator.generateMarkup(
                List.of(new SequenceDiagramMessage("here", "there", "(hello) a message", "message_id")),
                emptyList());
        assertThat(markup).isEqualToIgnoringWhitespace("@startuml group hello here ->> there:<text class=sequence_diagram_clickable sequence_diagram_message_id=message_id>(hello) a message</text> end @enduml");
    }

}
