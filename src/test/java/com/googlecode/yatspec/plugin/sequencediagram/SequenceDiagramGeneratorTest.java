package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.sequence.Participants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SequenceDiagramGeneratorTest {

    @Test
    void removesUnnecessaryParticipantsFromSvgDiagram() {
        SvgWrapper svgWrapper = new SequenceDiagramGenerator().generateSequenceDiagram(List.of(
                new SequenceDiagramMessage("Apple", "Banana", "messageName", "messageId")
        ), List.of(
                Participants.DATABASE.create("Apple"),
                Participants.DATABASE.create("Banana"),
                Participants.DATABASE.create("Pear")
        ));

        assertThat(svgWrapper.toString())
                .contains("Apple")
                .contains("Banana")
                .doesNotContain("Pear");
    }
}