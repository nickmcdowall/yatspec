package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenarioTest {

    @Test
    void uidIsDeterministic() {
        Scenario scenario1 = new Scenario("name", new JavaSource("value"));
        Scenario scenario2 = new Scenario("name", new JavaSource("value"));

        assertThat(scenario1.getUid()).isEqualTo(scenario2.getUid());
    }
}