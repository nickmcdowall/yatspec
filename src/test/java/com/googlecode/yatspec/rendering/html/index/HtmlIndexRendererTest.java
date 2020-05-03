package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlIndexRendererTest {

    private HtmlIndexRenderer htmlIndexRenderer = new HtmlIndexRenderer();
    private File tempDirectory = Files.newTemporaryFolder();

    @Test
    void generatesExpectedFiles() throws Exception {
        htmlIndexRenderer.complete(tempDirectory, aStubbedResult());

        assertThat(new File(tempDirectory, "index.css")).exists();
        assertThat(new File(tempDirectory, "index.js")).exists();
        assertThat(new File(tempDirectory, "index.html")).exists()
                .hasSameTextualContentAs(loadResource("com/googlecode/yatspec/rendering/html/index/expected-index.html"));
    }

    private File loadResource(String name) {
        return new File(getClass().getClassLoader()
                .getResource(name)
                .getFile()
        );
    }

    private Result aStubbedResult() {
        return new Result() {
            @Override
            public List<TestMethod> getTestMethods() {
                return List.of(
                        aStubbedMethod("aFirstDummyMethod"),
                        aStubbedMethod("aSecondDummyMethod")
                );
            }

            private TestMethod aStubbedMethod(String methodName) {
                return new TestMethod(
                        getClass(),
                        getClass().getEnclosingMethod(),
                        methodName,
                        new JavaSource(""),
                        new ScenarioTable()
                );
            }

            @Override
            public Class<?> getTestClass() {
                return getClass();
            }

            @Override
            public Scenario getScenario(String name) {
                return new Scenario("A scenario", new JavaSource(""));
            }

            @Override
            public String getName() {
                return "the name of the test";
            }

            @Override
            public String getPackageName() {
                return "com.an.example.package";
            }
        };
    }
}