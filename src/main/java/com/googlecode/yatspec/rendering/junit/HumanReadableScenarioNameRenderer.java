package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;

public class HumanReadableScenarioNameRenderer implements ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        String row = String.join(", ", scenarioName.getRow());

        return scenarioName.getMethodName() + "(" + row + ")";
    }
}
