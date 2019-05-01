package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.googlecode.yatspec.Creator.create;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;

public class DefaultResultListeners implements WithCustomResultListeners {
    public static final String RESULT_RENDER = "yatspec.result.renderer";
    public static final String INDEX_ENABLE = "yatspec.index.enable";
    public static final String INDEX_RENDER = "yatspec.index.renderer";

    @Override
    public Collection<SpecResultListener> getResultListeners() throws Exception {
        return Stream.of(resultListener(), indexListener())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<SpecResultListener> resultListener() throws Exception {
        return Optional.of(create(forName(getProperty(RESULT_RENDER, HtmlResultRenderer.class.getName()))));
    }

    private Optional<SpecResultListener> indexListener() throws Exception {
        if (!parseBoolean(getProperty(INDEX_ENABLE))) {
            return Optional.empty();
        }
        return Optional.of(create(forName(getProperty(INDEX_RENDER, HtmlIndexRenderer.class.getName()))));
    }
}
