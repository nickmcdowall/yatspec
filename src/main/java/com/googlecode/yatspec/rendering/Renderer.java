package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;

public interface Renderer<T> extends AttributeRenderer {
    String render(T t);

    default String toString(Object o) {
        return render((T) o);
    }

    default String toString(Object o, String formatName) {
        return toString(o);
    }
}
