package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.state.Result;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlValidatingResultRenderer extends HtmlResultRenderer {

    private final String expectedHtmlResult;

    public HtmlValidatingResultRenderer(String expectedHtmlFileName) throws IOException {
        expectedHtmlResult = loadResource(expectedHtmlFileName);
    }

    public String render(Result result) throws Exception {
        String actualHtml = super.render(result);
        assertThat(actualHtml).isXmlEqualTo(expectedHtmlResult);
        return actualHtml;
    }

    String loadResource(String name) throws IOException {
        InputStream resource = getClass().getResourceAsStream(name);
        return new String(resource.readAllBytes());
    }
}