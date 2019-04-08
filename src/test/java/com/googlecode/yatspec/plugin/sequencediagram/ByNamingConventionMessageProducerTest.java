package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Arrays;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ByNamingConventionMessageProducerTest {

    private static final int FIRST = 0;
    private static final int SECOND = 1;

    @Test
    void ignoresValuesWithoutNamingConvention() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("Shopping Basket", new Object());
        Object[] messages = new ByNamingConventionMessageProducer().messages(inputAndOutputs).toArray();

        assertThat(Arrays.isEmpty(messages), is(true));
    }

    @Test
    void convertsValuesWithNamingConventionToSequenceDiagramMessages() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("Kiss from Boy to Girl", new Object()).add("Slap from Girl to Boy", new Object());
        Object[] messages = new ByNamingConventionMessageProducer().messages(inputAndOutputs).toArray();
        assertThat(messages.length, is(equalTo(2)));
        assertThat(messages[FIRST], is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "Kiss", "Kiss_from_Boy_to_Girl"))));
        assertThat(messages[SECOND], is(equalTo(new SequenceDiagramMessage("Girl", "Boy", "Slap", "Slap_from_Girl_to_Boy"))));
    }

    @Test
    void dealsWithGroups() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("(grouped) Kiss from Boy to Girl", new Object());
        Object[] messages = new ByNamingConventionMessageProducer().messages(inputAndOutputs).toArray();
        assertThat(messages.length, is(equalTo(1)));
        assertThat(messages[FIRST], is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "(grouped) Kiss", "_grouped__Kiss_from_Boy_to_Girl"))));
    }
}
