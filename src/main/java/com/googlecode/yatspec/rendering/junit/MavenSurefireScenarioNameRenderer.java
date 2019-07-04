package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;

import static java.util.stream.Collectors.joining;

public class MavenSurefireScenarioNameRenderer implements ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        if (scenarioName.hasEmptyRow()) {
            return scenarioName.getMethodName();
        }

        String row = scenarioName.getRow().stream()
                .collect(joining("_"));

        return scenarioName.getMethodName() + "__" + row;
    }
}
