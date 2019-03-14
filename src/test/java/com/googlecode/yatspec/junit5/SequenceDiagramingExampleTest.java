package com.googlecode.yatspec.junit5;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.*;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Collection;

import static com.googlecode.totallylazy.Sequences.sequence;

@ExtendWith(SpecListener.class)
public class SequenceDiagramingExampleTest extends TestState implements WithCustomResultListeners {
    private static final Object ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST = new Object();

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    @BeforeEach
    public void setup() {
        sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @Test
    public void bambamGetsFoodForHisDad() throws Exception {
        String testNumber = "test:1";
        given(aHungryMrFlintstone());
        when(barney(), givesTheBurgerToBambam(testNumber));
        then(bambam(), givesFoodToMrFlintstone(testNumber));

        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney(testNumber));


        then(mrFlintstone(), sharesHisFoodWithBarneyBecauseHeLikesHim(testNumber));
    }

    @Test
    public void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleSequenceDiagramsOnOnePage() throws Exception {
        String testNumber = "test:2";
        given(aHungryMrFlintstone());
        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney(testNumber));

        when(barney(), givesTheBurgerToBambam(testNumber));
        then(bambam(), givesFoodToMrFlintstone(testNumber));

        then(mrFlintstone(), sharesHisFoodWithBarneyBecauseHeLikesHim(testNumber));
    }


    @Table({
            @Row({"row_a"}),
            @Row({"row_b"})
    })
    @ParameterizedTest
    public void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleScenariosPerTestMethod(String scenarioName) throws Exception {
        String testName = "test:3 scenario: " + scenarioName;
        given(aHungryMrFlintstone());
        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney(testName));

        when(barney(), givesTheBurgerToBambam(testName));
        then(bambam(), givesFoodToMrFlintstone(testName));

        then(mrFlintstone(), sharesHisFoodWithBarneyBecauseHeLikesHim(testName));
    }

    @Override
    public Collection<SpecResultListener> getResultListeners() throws Exception {
        return sequence(
                new HtmlResultRenderer().
                        withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows()).
                        withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer())).
                safeCast(SpecResultListener.class);
    }

    @AfterEach
    public void generateSequenceDiagram() {
        Sequence<SequenceDiagramMessage> messages = sequence(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs));
        capturedInputAndOutputs.add("Sequence Diagram", sequenceDiagramGenerator.generateSequenceDiagram(messages));
    }

    private Matcher<? super Object> sharesHisFoodWithBarneyBecauseHeLikesHim(String testNumber) {
        capturedInputAndOutputs.add("(grouped) food from mrflintstone to barney", "have some of my burger because I like you (test:" + testNumber + ")");
        return dummyMatcher();
    }

    private Matcher<? super Object> givesFoodToMrFlintstone(String testNumber) {
        capturedInputAndOutputs.add("(grouped) food from bambam to mrflintstone", "here is your burger (test:" + testNumber + ")");
        return dummyMatcher();
    }

    private Object givesTheBurgerToBambam(String testNumber) {
        capturedInputAndOutputs.add("burger from barney to bambam", "1 burger here u go (test:" + testNumber + ")");
        return ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
    }

    private Matcher<? super Object> placesABurgerOrderWithBarney(String testNumber) {
        capturedInputAndOutputs.add("burger order from bambam to barney", "Get me a burger (test:" + testNumber + ")");
        interestingGivens.add("burger");
        return dummyMatcher();
    }

    private ActionUnderTest heDemandsFoodFromBambam() {
        return (givens, capturedInputAndOutputs) -> {
            capturedInputAndOutputs.add("food demand from mrflintstone to bambam", "I want a burger");
            return capturedInputAndOutputs;
        };
    }


    private StateExtractor<Object> bambam() {
        return inputAndOutputs -> ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
    }

    private StateExtractor<Object> mrFlintstone() {
        return inputAndOutputs -> ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
    }

    private StateExtractor<Object> barney() {
        return inputAndOutputs -> ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
    }

    private GivensBuilder aHungryMrFlintstone() {
        return givens -> givens;
    }

    private void when(Object o, Object oo) {
    }

    private BaseMatcher<Object> dummyMatcher() {
        return new BaseMatcher<>() {
            public boolean matches(Object o) {
                return true;
            }

            public void describeTo(Description description) {
            }
        };
    }

}
