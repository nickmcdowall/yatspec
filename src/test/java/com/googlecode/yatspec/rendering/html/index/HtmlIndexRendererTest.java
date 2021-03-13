package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlFileRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.rootDirectoryFor;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlIndexRendererTest {

    private HtmlIndexRenderer htmlIndexRenderer = new HtmlIndexRenderer();
    private File tempDirectory = Files.newTemporaryFolder();

    @BeforeEach
    void setUp() {
        HtmlIndexRenderer.INDEX.reset();
    }

    @Test
    void generatesExpectedFiles() throws Exception {
        htmlIndexRenderer.complete(tempDirectory, aStubbedResult("com.an.example.package", "testMethodA"));
        htmlIndexRenderer.complete(tempDirectory, aStubbedResult("com.an.other.package", "testMethodB"));

        assertThat(new File(tempDirectory, "index.css")).exists();
        assertThat(new File(tempDirectory, "index.js")).exists();
        assertThat(new File(tempDirectory, "index.html")).exists()
                .hasSameTextualContentAs(loadResource("com/googlecode/yatspec/rendering/html/index/expected-index.html"));
    }

    @Test
    void indexContainsResultsFromAllIndexRenderers() throws Exception {
        new HtmlIndexRenderer().complete(tempDirectory, aStubbedResult("package1", "method1", "method2"));
        new HtmlIndexRenderer().complete(tempDirectory, aStubbedResult("package2", "method3"));

        assertThat(HtmlIndexRenderer.INDEX.entries()).hasSize(2);
    }

    private File loadResource(String name) {
        return new File(getClass().getClassLoader()
                .getResource(name)
                .getFile()
        );
    }

    private Result aStubbedResult(final String packageName, String... methodNames) {
        return new Result() {
            private final List<TestMethod> testMethods = stubbedMethods(methodNames);

            @Override
            public List<TestMethod> getTestMethods() {
                return testMethods;
            }

            @Override
            public Scenario getScenario(String name) {
                return null;
            }

            @Override
            public String getName() {
                return "the name of the test";
            }

            @Override
            public String getPackageName() {
                return packageName;
            }

            @Override
            public String getHtmlFileRelativePath() {
                return htmlFileRelativePath(getClass());
            }

            @Override
            public String getRootDirectory() {
                return rootDirectoryFor(getClass());
            }

            private List<TestMethod> stubbedMethods(String... names) {
                return Arrays.stream(names)
                        .map(this::aStubbedMethod)
                        .collect(toList());
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
        };
    }
}