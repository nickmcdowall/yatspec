package com.googlecode.yatspec.rendering;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;

public class Renderers {
    public static Callable2<EnhancedStringTemplateGroup, Pair<Predicate, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
        return (group, entry) -> group.registerRenderer(entry.first(), HtmlResultRenderer.callable(entry.second()));
    }
}
