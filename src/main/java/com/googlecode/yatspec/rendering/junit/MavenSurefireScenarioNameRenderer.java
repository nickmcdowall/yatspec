package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;

public class MavenSurefireScenarioNameRenderer implements ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        if (scenarioName.hasEmptyRow()) {
            return scenarioName.getMethodName();
        }

        String row = String.join("_", scenarioName.getRow());

        return scenarioName.getMethodName() + "__" + row;
    }
}
