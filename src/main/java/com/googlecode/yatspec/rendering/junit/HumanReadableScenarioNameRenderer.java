package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;

import static java.util.stream.Collectors.joining;

public class HumanReadableScenarioNameRenderer implements ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        String row = scenarioName.getRow().stream()
                .collect(joining(", "));

        return scenarioName.getMethodName() + "(" + row + ")";
    }
}
