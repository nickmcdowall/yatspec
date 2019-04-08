package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.state.ScenarioName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.googlecode.yatspec.fixture.RandomFixtures.anyString;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MavenSurefireScenarioNameRendererTest {

    private Renderer<ScenarioName> renderer;

    @BeforeEach
    void setUp() {
        renderer = new MavenSurefireScenarioNameRenderer();
    }

    @Test
    void rendersAScenarioNameWithoutArgs() throws Exception {
        String methodName = anyString();
        List<String> noArgs = emptyList();
        ScenarioName scenarioName = new ScenarioName(methodName, noArgs);

        String actualInvocationName = renderer.render(scenarioName);

        assertThat(actualInvocationName, is(methodName));
    }

    @Test
    void rendersAScenarioNameWithArgs() throws Exception {
        String methodName = anyString();
        String firstArg = anyString();
        String secondArg = anyString();
        String expectedInvocationName = String.format("%s__%s_%s", methodName, firstArg, secondArg);
        List<String> someArgs = Arrays.asList(firstArg, secondArg);
        ScenarioName scenarioName = new ScenarioName(methodName, someArgs);

        String actualInvocationName = renderer.render(scenarioName);

        assertThat(actualInvocationName, is(expectedInvocationName));
    }
}
