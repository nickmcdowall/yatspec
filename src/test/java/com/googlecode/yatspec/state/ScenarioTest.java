package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenarioTest {

    private final SvgWrapper stubDiagram = new SvgWrapper("a stub");
    private final Scenario scenario = new Scenario("name", new JavaSource("value"));

    private TestState testState = new TestState();


    @Test
    void uidIsDeterministic() {
        Scenario scenario1 = new Scenario("name", new JavaSource("value"));
        Scenario scenario2 = new Scenario("name", new JavaSource("value"));

        assertThat(scenario1.getUid()).isEqualTo(scenario2.getUid());
    }

    @Test
    void cloneSequenceDiagram() {
        testState.setDiagram(stubDiagram);
        scenario.copyTestState(testState);

        testState.reset();

        assertThat(scenario.getDiagram()).isEqualTo(stubDiagram);
    }

    @Test
    void cloneCapturedInputAndOutputValues() {
        testState.log("interaction 1", "value 1");

        scenario.copyTestState(testState);

        testState.reset();

        assertThat(scenario.getCapturedInputAndOutputs())
                .containsKeys("interaction 1")
                .containsValues("value 1");
    }

    @Test
    void cloneInterestingGivenValues() {
        TestState testState = new TestState();
        testState.interestingGivens().add("interesting thing 1", "interesting value 1");

        scenario.copyTestState(testState);

        testState.reset();

        assertThat(scenario.getInterestingGivens())
                .containsKeys("interesting thing 1")
                .containsValues("interesting value 1");
    }
}