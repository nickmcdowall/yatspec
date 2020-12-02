package com.googlecode.yatspec.rendering;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Predicate;

import java.util.AbstractMap.SimpleEntry;
import java.util.function.BiFunction;

public class Renderers {
    public static BiFunction<EnhancedStringTemplateGroup, SimpleEntry<Predicate, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
        return (group, entry) -> group.registerRenderer(entry.getKey(), renderer(entry.getValue()));
    }

    private static <T> com.googlecode.funclate.Renderer<? super T> renderer(final Renderer<T> value) {
        return value::render;
    }
}
