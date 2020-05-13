package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.state.Result;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Currently validation of HTML only enabled on OSX (includes the Travis CI build)
 * due to subtle differences in the generated output when using different OSs which
 * make it a nightmare to test the expectation.
 */
public class HtmlValidatingResultRenderer extends HtmlResultRenderer {

    public static final String MAC_OSX = "Mac OS X";
    public static final String OS_NAME = "os.name";

    private final InputStream expectedHtmlResult;

    public HtmlValidatingResultRenderer(String expectedHtmlFileName) {
        expectedHtmlResult = loadResource(expectedHtmlFileName);
    }

    public String render(Result result) throws Exception {
        String actualHtml = super.render(result);
        if (runningOn(MAC_OSX)) {
            testExpectationAgainst(new ByteArrayInputStream(actualHtml.getBytes()));
        }
        return actualHtml;
    }

    private boolean runningOn(String operatingSystem) {
        return operatingSystem.equalsIgnoreCase(System.getProperty(OS_NAME));
    }

    private void testExpectationAgainst(InputStream actualHtml) {
        assertThat(actualHtml).hasSameContentAs(expectedHtmlResult);
    }

    private InputStream loadResource(String name) {
        return getClass().getResourceAsStream(name);
    }
}