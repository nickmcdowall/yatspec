package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yatspec.junit.LinkingNote;

import java.io.File;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static java.lang.String.format;

public class LinkingNoteRenderer implements Renderer<LinkingNote> {

    private final Class<?> source;

    public LinkingNoteRenderer(Class<?> source) {
        this.source = source;
    }

    @Override
    public String render(LinkingNote linkingNoteNotes) {
        return format(linkingNoteNotes.message(), (Object[]) sequence(linkingNoteNotes.links()).map(link()).toArray(String.class));
    }

    private Callable1<Class, String> link() {
        return targetClass -> format("<a href='%s'>%s</a>",
                htmlResultFile(targetClass, source), wordify(targetClass.getSimpleName()));
    }

    private File htmlResultFile(Class resultClass, Class sourceClass) {
        return new File(getRootDirectoryPath(sourceClass) + htmlResultRelativePath(resultClass));
    }

    private String getRootDirectoryPath(Class sourceClass) {
        return sequence(htmlResultRelativePath(sourceClass).split("/"))
                .map(toParent())
                .toString("");
    }

    private Callable1<? super String, String> toParent() {
        return (Callable1<String, String>) s -> {
            if (s.contains(".")) {
                return "";
            }
            return "../";
        };
    }
}
