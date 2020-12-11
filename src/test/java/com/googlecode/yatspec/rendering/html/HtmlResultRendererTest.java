package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

class HtmlResultRendererTest {

    private static final String CUSTOM_RENDERED_TEXT = "some crazy and likely random string that wouldn't appear in the html";

    @Test
    void providesLinksToResultOutputRelativeToOutputDirectory() {
        assertThat(
                HtmlResultRenderer.htmlResultRelativePath(this.getClass()),
                is(Paths.get("com/googlecode/yatspec/rendering/html/HtmlResultRendererTest.html").toString()));
    }

    @Test
    void loadsTemplateOffClassPath() throws Exception {
        TestResult result = new TestResult(this.getClass());

        String html = new HtmlResultRenderer().render(result);

        assertThat(html, is(not(nullValue())));
    }

    @Test
    void supportsCustomRenderingOfScenarioLogs() throws Exception {
        TestResult testResult = aTestResultWithCustomRenderTypeAddedToScenarioLogs(new RenderedType());

        String html = new HtmlResultRenderer().
                withCustomRenderer(RenderedType.class, result -> new DefaultReturningRenderer(CUSTOM_RENDERED_TEXT)).
                render(testResult);

        assertThat(html, containsString(CUSTOM_RENDERED_TEXT));
    }

    private TestResult aTestResultWithCustomRenderTypeAddedToScenarioLogs(RenderedType thingToBeCustomRendered) {
        TestResult result = new TestResult(getClass());
        addToCapturedInputsAndOutputs(result, thingToBeCustomRendered);
        return result;
    }

    private void addToCapturedInputsAndOutputs(TestResult result, Object thingToBeCustomRendered) {
        Scenario scenario = result.getTestMethods().get(0).getScenarios().get(0);
        TestState testState = new TestState();
        testState.log("custom rendered thing", thingToBeCustomRendered);
        scenario.copyTestState(testState);
    }

    static class RenderedType {
    }

    private class DefaultReturningRenderer implements Renderer<RenderedType> {
        private String rendererOutput;

        DefaultReturningRenderer(final String rendererOutput) {
            this.rendererOutput = rendererOutput;
        }

        public String render(RenderedType renderedType) {
            return rendererOutput;
        }
    }
}
