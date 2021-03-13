package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.junit.LinkingNote;

import java.io.File;
import java.util.function.Function;

import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlFileRelativePath;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public class LinkingNoteRenderer implements Renderer<LinkingNote> {

    private final String rootDirectory;

    public LinkingNoteRenderer(String rootDirectory) {
        this.rootDirectory = rootDirectory;
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
                htmlResultFile(rootDirectory + htmlFileRelativePath(targetClass)), wordify(targetClass.getSimpleName()));
    }

    private File htmlResultFile(String pathname) {
        return new File(pathname);
    }
}
