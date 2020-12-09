package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class HyperlinkRendererTest {
    private static final String SINGLE_REFERENCE_URL_FORMAT = "http://myserver.com/browse/%s";
    private static final String MULTIPLE_REFERENCE_URL_FORMAT = "http://myserver.com/browse/%s?id=%s";
    private static final String REGEX = "((?i)bug)-[0-9]{3,5}";
    private static final Renderer<String> DELEGATE_RENDERER = instance -> isEmpty(instance) ? "" : instance;

    @Test
    void shouldNotRenderNullAsHyperlink() {
        assertRenders(new HyperlinkRenderer<>(SINGLE_REFERENCE_URL_FORMAT, DELEGATE_RENDERER), null, "");
    }

    @Test
    void shouldRenderAnyUrlMatchAsHyperlink() {
        assertRenders(new HyperlinkRenderer<>(SINGLE_REFERENCE_URL_FORMAT, DELEGATE_RENDERER), "BUG-2112", "<a href='http://myserver.com/browse/BUG-2112'>BUG-2112</a>");
        assertRenders(new HyperlinkRenderer<>(SINGLE_REFERENCE_URL_FORMAT, DELEGATE_RENDERER), "My BUG-2112", "<a href='http://myserver.com/browse/My BUG-2112'>My BUG-2112</a>");
    }

    @Test
    void allowsSpecifyingReplacementPattern() {
        assertRenders(new HyperlinkRenderer<>(DELEGATE_RENDERER, "(?:#)([0-9]+)", "<a href='http://myserver.com/$1'>$1</a>"), "text #901 text", "text <a href='http://myserver.com/901'>901</a> text");
    }

    @Test
    void shouldNotRenderNoUrlAsAHyperlink() {
        assertRenders(SINGLE_REFERENCE_URL_FORMAT, "nothing", "nothing");
    }

    @Test
    void shouldRenderAUrlMatchAsHyperlink() {
        assertRenders(SINGLE_REFERENCE_URL_FORMAT, "BUG-2112", "<a href='http://myserver.com/browse/BUG-2112'>BUG-2112</a>");
        assertRenders(SINGLE_REFERENCE_URL_FORMAT, "BUG-2112 - also involved BUG-2312 as well", "<a href='http://myserver.com/browse/BUG-2112'>BUG-2112</a> - also involved <a href='http://myserver.com/browse/BUG-2312'>BUG-2312</a> as well");
    }

    @Test
    void shouldRenderMultipleUrlMatchesAsHyperlink() {
        assertRenders(MULTIPLE_REFERENCE_URL_FORMAT, "BUG-2112", "<a href='http://myserver.com/browse/BUG-2112?id=BUG-2112'>BUG-2112</a>");
    }

    private static void assertRenders(String urlFormat, String value, String expected) {
        assertRenders(new HyperlinkRenderer<>(urlFormat, REGEX, DELEGATE_RENDERER), value, expected);
    }

    private static void assertRenders(Renderer<String> renderer, String value, String expected) {
        assertThat(renderer.render(value), equalTo(expected));
    }
}