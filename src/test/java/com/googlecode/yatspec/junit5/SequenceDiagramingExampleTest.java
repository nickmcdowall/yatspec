package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.*;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlValidatingResultRenderer;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Collection;
import java.util.List;

import static com.googlecode.yatspec.sequence.Participants.ACTOR;


@ExtendWith(SequenceDiagramExtension.class)
class SequenceDiagramingExampleTest implements WithParticipants, WithCustomResultListeners {

    private final TestState interactions = new TestState();
    private final InterestingGivens interestingGivens = interactions.interestingGivens();

    @Test
    @Notes("Note that this test may make you feel hungry")
    @LinkingNote(message = "For related test class see %s",
            links = {SpecificationExampleTest.class}
    )
    void bambamGetsFoodForHisDad() {
        givenMrFlintstoneIs("hungry");
        whenHeHarassesBambamForFood();
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
        whenHeHarassesBambamForFood();
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
        interactions.log("/food-order?love=pie&like=mash from bambam to barney", "Get me " + foodItem);
    }

    private void whenHeHarassesBambamForFood() {
        interactions.log("/food-demand from mrflintstone to bambam", "I want a burger");
        interactions.log("/food-demand from mrflintstone to bambam", "I need a burger");
        interactions.log("/food-demand from mrflintstone to bambam", "I demand a burger");
    }

    private void givenMrFlintstoneIs(String feeling) {
        interestingGivens.add("Flintstone is " + feeling);
        interestingGivens.add("burger");
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
                new HtmlValidatingResultRenderer("/expected/SequenceDiagramingExampleTest.html")
                        .withCustomRenderer(SvgWrapper.class::isInstance, result -> new DontHighlightRenderer())
        );
    }

}
