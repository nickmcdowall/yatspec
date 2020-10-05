package com.googlecode.yatspec.state;

import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class LogKeyTest {

    private String troublesomeKeyValue = "GET /some-path?cat&dog () from A to B.c";

    @Test
    void shouldReplaceSpecialCharactersThatCauseIssues() {
        String logKeyValue = new LogKey(troublesomeKeyValue).getHtmlSafeValue();

        assertThat(logKeyValue)
                .doesNotContain("/")
                .doesNotContain("-")
                .doesNotContain(" ")
                .doesNotContain("?")
                .doesNotContain("(")
                .doesNotContain(".")
                .doesNotContain(")");
    }

    @Test
    void produceSameLogKeyValueAsMessageIdFromByNamingConventionMessageProducer() {
        Collection<SequenceDiagramMessage> messages = new ByNamingConventionMessageProducer().messages(
                new CapturedInputAndOutputs().add(troublesomeKeyValue, new Object())
        );

        String logKeyValue = new LogKey(troublesomeKeyValue).getHtmlSafeValue();

        assertThat(logKeyValue).isEqualTo(messages.stream()
                .map(SequenceDiagramMessage::getMessageId)
                .findFirst()
                .orElse(""));
    }
}