package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.rendering.LinkingNoteRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static com.googlecode.yatspec.parsing.TestParser.parseTestMethods;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpecListener.class)
class LinkingNoteRendererTest {
    @Test
    @LinkingNote(message = "Classes specified in the linking note should result in links to the resulted yatspec output eg. %s, %s", links = {String.class, Integer.class})
    void shouldRenderLinkedNotesAsNotesWithLinksToTheYatspecOutputOfTheTestClassesSpecified() throws Exception {
        assertThat(theRenderedValueOfTheLinkingNoteOfThisMethod(),
                is("Classes specified in the linking note should result in links to the resulted yatspec output eg. <a href='../../../../java/lang/String.html'>String</a>, <a href='../../../../java/lang/Integer.html'>Integer</a>"));
    }

    private String theRenderedValueOfTheLinkingNoteOfThisMethod() throws Exception {
        Optional<LinkingNote> linkingNote = parseTestMethods(getClass()).stream()
                .flatMap(testMethod -> testMethod.getAnnotations().stream())
                .filter(LinkingNote.class::isInstance)
                .map(LinkingNote.class::cast)
                .findFirst();

        return new LinkingNoteRenderer(LinkingNoteRendererTest.class).render(linkingNote.get());
    }
}
