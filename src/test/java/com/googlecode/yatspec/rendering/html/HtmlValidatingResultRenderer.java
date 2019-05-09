package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.state.Result;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlValidatingResultRenderer extends HtmlResultRenderer {

    public static final String HTML_COMMENTS = "(?s)<!--.*?-->";
    public static final String EMPTY = "";

    private final String expectedHtmlResult;

    public HtmlValidatingResultRenderer(String expectedHtmlFileName) throws IOException {
        expectedHtmlResult = loadResource(expectedHtmlFileName);
    }

    public String render(Result result) throws Exception {
        String actualHtml = super.render(result);
        assertThat(actualHtml.replaceAll(HTML_COMMENTS, EMPTY)).isEqualToIgnoringWhitespace(expectedHtmlResult.replaceAll(HTML_COMMENTS, EMPTY));
        return actualHtml;
    }

    String loadResource(String name) throws IOException {
        InputStream resource = getClass().getResourceAsStream(name);
        return new String(resource.readAllBytes());
    }
}