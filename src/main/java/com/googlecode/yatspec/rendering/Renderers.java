package com.googlecode.yatspec.rendering;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;

import java.util.AbstractMap.SimpleEntry;
import java.util.function.BiFunction;

public class Renderers {
    public static BiFunction<EnhancedStringTemplateGroup, SimpleEntry<Predicate, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
        return (group, entry) -> group.registerRenderer(entry.getKey(), HtmlResultRenderer.callable(entry.getValue()));
    }
}
