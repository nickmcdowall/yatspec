package com.googlecode.yatspec.rendering;

public class ToStringRenderer<T> implements Renderer<T> {
    public String render(T instance) {
        return instance != null ? instance.toString() : "";
    }
}
