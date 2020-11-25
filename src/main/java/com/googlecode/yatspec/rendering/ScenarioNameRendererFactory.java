package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.rendering.junit.HumanReadableScenarioNameRenderer;

import static com.googlecode.yatspec.Creator.create;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;

public class ScenarioNameRendererFactory {
    public static final String SCENARIO_NAME_RENDERER = "yatspec.scenario.name.renderer";

    public static ScenarioNameRenderer renderer() {
        ScenarioNameRenderer renderer;
        try {
            renderer = create(forName(getProperty(SCENARIO_NAME_RENDERER, HumanReadableScenarioNameRenderer.class.getName())));
        } catch (Exception e) {
            renderer = new HumanReadableScenarioNameRenderer();
        }
        return renderer;
    }
}
