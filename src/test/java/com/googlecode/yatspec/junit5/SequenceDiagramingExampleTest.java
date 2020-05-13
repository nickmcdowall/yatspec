package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.*;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlValidatingResultRenderer;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Collection;
import java.util.List;

import static com.googlecode.yatspec.sequence.Participants.ACTOR;


@ExtendWith(SequenceDiagramExtension.class)
class SequenceDiagramingExampleTest implements WithTestState, WithParticipants, WithCustomResultListeners {

    private final TestState interactions = new TestState();
    private final InterestingGivens interestingGivens = interactions.interestingGivens();

    @Test
    void bambamGetsFoodForHisDad() {
        givenMrFlintstoneIs("hungry");
        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("a burger");

        whenBarneyGivesToBambam("1 burger");
        thenBambamGivesToMrFlintstone("1 burger");
        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim();
    }

    @Table({
            @Row({"starving"}),
            @Row({"peckish"})
    })
    @ParameterizedTest
    void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleScenariosPerTestMethod(String feeling) {
        givenMrFlintstoneIs(feeling);
        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("a burger");

        whenBarneyGivesToBambam("1 burger");
        thenBambamGivesToMrFlintstone("1 burger");

        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim();
    }

    private void thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim() {
        interactions.log("(kindness) bite of burger from mrflintstone to barney", "have some of my burger because I like you");
    }

    private void thenBambamGivesToMrFlintstone(String foodItem) {
        interactions.log("(kindness) response from bambam to mrflintstone", "here is your " + foodItem);
    }

    private void whenBarneyGivesToBambam(String foodItem) {
        interactions.log("(kindness) food response from barney to bambam", foodItem + " here u go");
    }

    private void thenBambamPlacesABurgerOrderWithBarney(String foodItem) {
        interactions.log("food order from bambam to barney", "Get me " + foodItem);
    }

    private void whenHeDemandsFoodFromBambam() {
        interactions.log("food demand from mrflintstone to bambam", "I want a burger");
    }

    private void givenMrFlintstoneIs(String feeling) {
        interestingGivens.add("Flintstone is " + feeling);
        interestingGivens.add("burger");
    }

    @Override
    public TestState testState() {
        return interactions;
    }

    @Override
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("mrflintstone", "Mr (greedy) Flintstone"),
                ACTOR.create("bambam"),
                ACTOR.create("barney")
        );
    }

    @Override
    public Collection<SpecResultListener> getResultListeners() {
        return List.of(
                new HtmlValidatingResultRenderer("/expected/SequenceDiagramingExampleTest.html").
                        withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer())
        );
    }

}
