package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

@ExtendWith({SpecListener.class, SequenceDiagramExtension.class})
public class SequenceDiagramingExampleTest implements WithTestState {

    private TestState interactions = new TestState();

    @Test
    void bambamGetsFoodForHisDad() {
        String testNumber = "test:1";
        givenAHungryMrFlintstone();
        whenBarneyGivesTheBurgerToBambam("1 burger here u go (test:" + testNumber + ")");
        thenBambamGivesFoodToMrFlintstone("here is your burger (test:" + testNumber + ")");

        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("Get me a burger (test:" + testNumber + ")");
        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim("have some of my burger because I like you (test:" + testNumber + ")");
    }

    @Test
    void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleSequenceDiagramsOnOnePage() {
        String testNumber = "(test:2)";
        givenAHungryMrFlintstone();
        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("Get me a burger " + testNumber);

        whenBarneyGivesTheBurgerToBambam("1 burger here u go " + testNumber);
        thenBambamGivesFoodToMrFlintstone("here is your burger " + testNumber);
        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim("have some of my burger because I like you " + testNumber);
    }

    @Table({
            @Row({"row_a"}),
            @Row({"row_b"})
    })
    @ParameterizedTest
    void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleScenariosPerTestMethod(String scenarioName) {
        String testName = "test:3 scenario: " + scenarioName;
        givenAHungryMrFlintstone();
        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("Get me a burger (test:" + testName + ")");

        whenBarneyGivesTheBurgerToBambam("1 burger here u go (test:" + testName + ")");
        thenBambamGivesFoodToMrFlintstone("here is your burger (test:" + testName + ")");

        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim("have some of my burger because I like you (test:" + testName + ")");
    }

    private void thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim(String food) {
        interactions.log("(grouped) food from mrflintstone to barney", food);
    }

    private void thenBambamGivesFoodToMrFlintstone(String food) {
        interactions.log("(grouped) food from bambam to mrflintstone", food);
    }

    private void whenBarneyGivesTheBurgerToBambam(String burger) {
        interactions.log("burger from barney to bambam", burger);
    }

    private void thenBambamPlacesABurgerOrderWithBarney(String burgerOrder) {
        interactions.log("burger order from bambam to barney", burgerOrder);
        interactions.interestingGivens.add("burger");
    }

    private void whenHeDemandsFoodFromBambam() {
        interactions.log("food demand from mrflintstone to bambam", "I want a burger");
    }

    private void givenAHungryMrFlintstone() {
        interactions.interestingGivens.add("Flintstone is very hungry");
    }

    @Override
    public TestState testState() {
        return interactions;
    }
}
