package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;

import java.text.MessageFormat;
import java.util.Optional;

public class DontHighlightRenderer<T> implements Renderer<T> {
    public String render(T instance) {
        return MessageFormat.format("<div class=''nohighlight''>{0}</div>", Optional.ofNullable(instance)
                .map(Object::toString)
                .orElse(""));
    }
}
