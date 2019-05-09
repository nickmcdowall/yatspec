package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.state.Result;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Currently validation of HTML only enabled on OSX (includes the Travis CI build)
 * due to subtle differences in the generated output when using different OSs which
 * make it a nightmare to test the expectation.
 */
public class HtmlValidatingResultRenderer extends HtmlResultRenderer {

    public static final String HTML_COMMENTS = "(?s)<!--.*?-->";
    public static final String EMPTY = "";
    public static final String MAC_OSX = "Mac OS X";
    public static final String OS_NAME = "os.name";

    private final String expectedHtmlResult;

    public HtmlValidatingResultRenderer(String expectedHtmlFileName) throws IOException {
        expectedHtmlResult = loadResource(expectedHtmlFileName);
    }

    public String render(Result result) throws Exception {
        String actualHtml = super.render(result);
        if (runningOn(MAC_OSX)) {
            testExpectation(actualHtml);
        }
        return actualHtml;
    }

    public boolean runningOn(String operatingSystem) {
        return operatingSystem.equalsIgnoreCase(System.getProperty(OS_NAME));
    }

    public void testExpectation(String actualHtml) {
        assertThat(actualHtml.replaceAll(HTML_COMMENTS, EMPTY)).isEqualToIgnoringWhitespace(expectedHtmlResult.replaceAll(HTML_COMMENTS, EMPTY));
    }

    String loadResource(String name) throws IOException {
        InputStream resource = getClass().getResourceAsStream(name);
        return new String(resource.readAllBytes());
    }
}