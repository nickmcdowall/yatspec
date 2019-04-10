package com.googlecode.yatspec.plugin.sequencediagram;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class SequenceDiagramMessageTest {

    @Test
    void createsExpectedMarkup() {
        SequenceDiagramMessage message = new SequenceDiagramMessage("client", "server", "get request", "sends_auth_request");

        assertThat(message.toPlantUmlMarkup(), Matchers.equalTo("client ->> server:<text class=sequence_diagram_clickable sequence_diagram_message_id=sends_auth_request>get request</text>"));
    }

    @Test
    void usesDottedArrowForResponses() {
        SequenceDiagramMessage message = new SequenceDiagramMessage("client", "server", "get response", "sends_auth_response");

        assertThat(message.toPlantUmlMarkup(), Matchers.equalTo("client -->> server:<text class=sequence_diagram_clickable sequence_diagram_message_id=sends_auth_response>get response</text>"));
    }
}