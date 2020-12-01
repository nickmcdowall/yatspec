package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

class NotesTagFinderTest {

    private final NotesTagFinder notesTagFinder = new NotesTagFinder();

    @Notes("I have two tags #tag1 and #tag2")
    @Test
    void findsAllNoteTags() {
        Collection<String> tags = notesTagFinder.tags(methodWithNotes());

        assertThat(tags).containsExactly("#tag1", "#tag2");
    }

    private TestMethod methodWithNotes() {
        return stream(getClass().getDeclaredMethods())
                .filter(method -> stream(method.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(Notes.class)))
                .map(this::aTestMethodWrapper)
                .findFirst().orElseThrow();
    }

    private TestMethod aTestMethodWrapper(Method method) {
        return new TestMethod(
                method.getDeclaringClass(),
                method,
                method.getName(),
                new JavaSource(""),
                new ScenarioTable()
        );
    }
}