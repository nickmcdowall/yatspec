package com.googlecode.yatspec.rendering.html;


import com.googlecode.yatspec.parsing.TestText;
import org.junit.jupiter.api.Test;

import static com.googlecode.yatspec.rendering.html.JavaSourceRenderer.removeDotClass;
import static org.assertj.core.api.Assertions.assertThat;

class TestTextRendererTest {

    private final JavaSourceRenderer renderer = new JavaSourceRenderer();

    @Test
    void noLongerEscapeXmlCharactersInRenderer() {
        assertThat(renderer.render(new TestText("<abc>&:</abc>\\+:\n"))).isEqualTo("<abc>& </abc>\\+");
    }

    @Test
    void shouldRemoveDotClassFromRenderedOutput() {
        assertThat(removeDotClass("get(SomeThing.class)")).isEqualTo("get(SomeThing)");
        assertThat(removeDotClass("SomeThing.class")).isEqualTo("SomeThing");
        assertThat(removeDotClass("get(SomeThing.class).get(SomeThing.class)")).isEqualTo("get(SomeThing).get(SomeThing)");
        assertThat(removeDotClass("variable.classButActuallyMethod()")).isEqualTo("variable.classButActuallyMethod()");
        assertThat(removeDotClass("variable.class_weird()")).isEqualTo("variable.class_weird()");
    }

}
