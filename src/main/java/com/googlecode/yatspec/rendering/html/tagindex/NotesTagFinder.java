package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.totallylazy.regex.Matches;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.TestMethod;

import java.util.Collection;
import java.util.function.Function;
import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.regex.Regex.regex;
import static java.util.Collections.emptyList;

public class NotesTagFinder implements TagFinder {
    private final Regex regex;

    NotesTagFinder() {
        this("#[^\\s]+");
    }

    private NotesTagFinder(String regex) {
        this.regex = regex(regex);
    }

    public Collection<String> tags(TestMethod testMethod) {
        return Notes.methods.notes(testMethod.getAnnotations())
                .map(notesToTags())
                .orElse(emptyList());
    }

    private Function<Notes, Collection<String>> notesToTags() {
        return notes -> {
            Matches matches = regex.findMatches(notes.value());
            return matches.map(MatchResult::group);
        };
    }

}
