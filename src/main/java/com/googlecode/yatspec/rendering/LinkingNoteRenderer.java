package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.junit.LinkingNote;

import java.io.File;
import java.util.function.Function;

import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class LinkingNoteRenderer implements Renderer<LinkingNote> {

    private final Class<?> source;

    public LinkingNoteRenderer(Class<?> source) {
        this.source = source;
    }

    @Override
    public String render(LinkingNote linkingNoteNotes) {
        return format(linkingNoteNotes.message(), arrayOfHyperlinks(linkingNoteNotes));
    }

    private Object[] arrayOfHyperlinks(LinkingNote linkingNoteNotes) {
        return stream(linkingNoteNotes.links())
                .map(toHyperlink())
                .toArray(String[]::new);
    }

    private Function<Class<?>, String> toHyperlink() {
        return targetClass -> format("<a href='%s'>%s</a>",
                htmlResultFile(targetClass, source), wordify(targetClass.getSimpleName()));
    }

    private File htmlResultFile(Class<?> resultClass, Class<?> sourceClass) {
        return new File(getRootDirectoryPath(sourceClass) + htmlResultRelativePath(resultClass));
    }

    private String getRootDirectoryPath(Class<?> sourceClass) {
        return stream(htmlResultRelativePath(sourceClass).split("/"))
                .map(s -> s.contains(".") ? "" : "../")
                .collect(joining());
    }
}
