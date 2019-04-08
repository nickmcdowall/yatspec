package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.yatspec.parsing.JavaSource;
import org.junit.jupiter.api.Test;

import static java.lang.System.lineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JavaSourceRendererTest {

    @Test
    void removesFirstLevelOfIndentation() {
        String withIndentation = "\tFoo" + lineSeparator() + "\tBar";
        String withoutIndentation = "Foo" + lineSeparator() + "Bar";
        assertThat(new JavaSourceRenderer().render(new JavaSource(withIndentation)), is(withoutIndentation));
    }

    @Test
    void removesLeadingAndTrailingBlankLines() {
        assertThat(new JavaSourceRenderer().render(new JavaSource("\n\nFoo\nBar\n\n")), is("Foo\nBar"));
    }
}
