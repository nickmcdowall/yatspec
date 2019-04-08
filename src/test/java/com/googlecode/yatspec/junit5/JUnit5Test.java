package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.*;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.HyperlinkRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.tagindex.HtmlTagIndexRenderer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpecListener.class)
class JUnit5Test implements WithCustomResultListeners {

    private static final String NOTE_REGEX_PATTERN = "(?:#)([^\\s]+)";
    private static final String REPLACEMENT_PATTERN = "<a href='http://localhost:8080/pretent-issue-tracking/$1'>$1</a>";

    @ParameterizedTest
    @Table({
            @Row({"1", "1", "2"}),
            @Row({"2", "3", "5"})
    })
    void sum(int a, int b, int sum) {
        assertEquals(sum, a + b);
    }

    @Override
    public Collection<SpecResultListener> getResultListeners() {
        return List.of(
                new HtmlResultRenderer().
                        withCustomRenderer(Notes.class, new HyperlinkRenderer(new NotesRenderer(), NOTE_REGEX_PATTERN, REPLACEMENT_PATTERN)),
                new HtmlIndexRenderer(),
                new HtmlTagIndexRenderer());
    }
}
