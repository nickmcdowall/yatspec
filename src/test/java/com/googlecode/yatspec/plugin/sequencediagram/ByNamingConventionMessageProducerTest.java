package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ByNamingConventionMessageProducerTest {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;

    @Test
    void ignoresValuesWithoutNamingConvention() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("Shopping Basket", new Object());

        Collection<SequenceDiagramMessage> messages = new ByNamingConventionMessageProducer().messages(inputAndOutputs);

        assertThat(messages, is(empty()));
    }

    @Test
    void convertsValuesWithNamingConventionToSequenceDiagramMessages() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs()
                .add("Kiss from Boy to Girl", new Object())
                .add("Slap from Girl to Boy", new Object())
                .add("GET /some-path from Boy to Girl", new Object());

        Object[] messages = new ByNamingConventionMessageProducer().messages(inputAndOutputs).toArray();

        assertThat(messages.length, is(equalTo(3)));
        assertThat(messages[FIRST], is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "Kiss", "Kiss_from_Boy_to_Girl"))));
        assertThat(messages[SECOND], is(equalTo(new SequenceDiagramMessage("Girl", "Boy", "Slap", "Slap_from_Girl_to_Boy"))));
        assertThat(messages[THIRD], is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "GET /some-path", "GET__some_path_from_Boy_to_Girl"))));
    }

    @Test
    void dealsWithGroups() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("(grouped) Kiss from Boy to Girl", new Object());
        Object[] messages = new ByNamingConventionMessageProducer().messages(inputAndOutputs).toArray();
        assertThat(messages.length, is(equalTo(1)));
        assertThat(messages[FIRST], is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "(grouped) Kiss", "_grouped__Kiss_from_Boy_to_Girl"))));
    }
}
