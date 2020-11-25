package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.ToStringRenderer;

import java.text.MessageFormat;

public class DontHighlightRenderer<T> implements Renderer<T> {
    public String render(T instance) {
        return MessageFormat.format("<div class=''nohighlight''>{0}</div>", new ToStringRenderer().render(instance));
    }
}
