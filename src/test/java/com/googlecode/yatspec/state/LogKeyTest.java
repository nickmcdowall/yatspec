package com.googlecode.yatspec.state;

import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class LogKeyTest {

    @Test
    void shouldRemoveSpecialCharactersThatCauseIssues() {
        String logKeyValue = new LogKey("GET /some/path () from A to B").getValueWithSpacesReplacedByUnderscore();

        assertThat(logKeyValue)
                .doesNotContain("/")
                .doesNotContain(" ")
                .doesNotContain("(")
                .doesNotContain(")");
    }

    @Test
    void produceSameLogKeyValueAsMessageIdFromByNamingConventionMessageProducer() {
        Collection<SequenceDiagramMessage> messages = new ByNamingConventionMessageProducer().messages(
                new CapturedInputAndOutputs().add("GET /some/path () from A to B", new Object())
        );

        String logKeyValue = new LogKey("GET /some/path () from A to B").getValueWithSpacesReplacedByUnderscore();

        assertThat(logKeyValue).isEqualTo(messages.stream()
                .map(SequenceDiagramMessage::getMessageId)
                .findFirst()
                .orElse(""));
    }
}