package com.googlecode.yatspec.rendering.html;


import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class JavaSourceRendererTest {

    @Test
    void shouldRemoveDotClassFromRenderedOutput() {
        assertThat(renderedVersionOf("get(SomeThing.class)"), is("get(SomeThing)"));
        assertThat(renderedVersionOf("SomeThing.class"), is("SomeThing"));
        assertThat(renderedVersionOf("get(SomeThing.class).get(SomeThing.class)"), is("get(SomeThing).get(SomeThing)"));
        assertThat(renderedVersionOf("variable.classButActuallyMethod()"), is("variable.classButActuallyMethod()"));
        assertThat(renderedVersionOf("variable.class_weird()"), is("variable.class_weird()"));
    }

    private String renderedVersionOf(String s) {
        return JavaSourceRenderer.removeDotClass(s);
    }
}
