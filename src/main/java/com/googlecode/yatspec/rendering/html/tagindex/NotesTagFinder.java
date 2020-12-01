package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.TestMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;

public class NotesTagFinder implements TagFinder {

    public static final String DEFAULT_TAG_REGEX = "#[^\\s]+";

    private final Pattern pattern;

    NotesTagFinder() {
        this(DEFAULT_TAG_REGEX);
    }

    private NotesTagFinder(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Collection<String> tags(TestMethod testMethod) {
        return Notes.methods.notes(testMethod.getAnnotations())
                .map(this::notesToTags)
                .orElse(emptyList());
    }

    private Collection<String> notesToTags(Notes notes) {
        Matcher tagMatcher = pattern.matcher(notes.value());
        List<String> tags = new ArrayList<>();
        while (tagMatcher.find()) {
            tags.add(tagMatcher.toMatchResult().group());
        }
        return tags;
    }
}
