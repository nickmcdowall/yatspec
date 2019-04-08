package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

@ExtendWith({SpecListener.class, SequenceDiagramExtension.class})
class SequenceDiagramingExampleTest implements WithTestState {

    private TestState interactions = new TestState();
    private InterestingGivens interestingGivens = interactions.interestingGivens;

    @Test
    void bambamGetsFoodForHisDad() {
        givenMrFlintstoneIs("very hungry");
        whenBarneyGivesToBambam("2 burgers");
        thenBambamGivesToMrFlintstone("2 burgers");

        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("a burger");
        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim();
    }

    @Test
    void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleSequenceDiagramsOnOnePage() {
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
        interactions.log("(kindness) " + foodItem + " from bambam to mrflintstone", "here is your " + foodItem);
    }

    private void whenBarneyGivesToBambam(String foodItem) {
        interactions.log(foodItem + " from barney to bambam", foodItem + " here u go");
    }

    private void thenBambamPlacesABurgerOrderWithBarney(String foodItem) {
        interactions.log("food order from bambam to barney", "Get me " + foodItem);
    }

    private void whenHeDemandsFoodFromBambam() {
        interactions.log("food demand from mrflintstone to bambam", "I want a burger");
    }

    private void givenMrFlintstoneIs(String feeling) {
        interestingGivens.add("Flintstone is " + feeling);
    }

    @Override
    public TestState testState() {
        return interactions;
    }
}
