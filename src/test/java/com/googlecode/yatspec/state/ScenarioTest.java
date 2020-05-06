package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenarioTest {

    private final SvgWrapper stubDiagram = new SvgWrapper("");
    private final Scenario scenario = new Scenario("name", new JavaSource("value"));

    @Test
    void uidIsDeterministic() {
        Scenario scenario1 = new Scenario("name", new JavaSource("value"));
        Scenario scenario2 = new Scenario("name", new JavaSource("value"));

        assertThat(scenario1.getUid()).isEqualTo(scenario2.getUid());
    }

    @Test
    void returnsSequenceDiagramWhenAvailable() {
        scenario.setTestState(testStateWith(stubDiagram));

        assertThat(scenario.getDiagram()).isEqualTo(stubDiagram);
    }

    private TestState testStateWith(SvgWrapper stubDiagram) {
        TestState testState = new TestState();
        testState.setDiagram(stubDiagram);
        return testState;
    }
}