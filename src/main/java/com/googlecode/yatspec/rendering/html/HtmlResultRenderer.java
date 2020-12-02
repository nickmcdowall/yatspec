package com.googlecode.yatspec.rendering.html;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.yatspec.Creator;
import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.FilesUtil;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.*;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.ScenarioTableHeader;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.text.StringEscapeUtils;
import org.jdom.Document;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.function.Predicate;

import static com.googlecode.yatspec.parsing.FilesUtil.overwrite;
import static java.lang.String.format;

public class HtmlResultRenderer implements SpecResultListener {

    private final List<SimpleEntry<Predicate, Renderer>> customRenderers = new ArrayList<>();

    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        File htmlResultFile = htmlResultFile(yatspecOutputDir, result.getTestClass());
        overwrite(htmlResultFile, render(result));
        addAdjacentFile(htmlResultFile, "yatspec_classic.css");
        addAdjacentFile(htmlResultFile, "yatspec_alt.css");
        addAdjacentFile(htmlResultFile, "yatspec.js");
        addAdjacentFile(htmlResultFile, "xregexp.js");
    }

    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.setRootDir(null); //forces use of classpath to lookup template
        group.registerRenderer(o -> !(o instanceof Number), renderer(StringEscapeUtils::escapeXml11));
        group.registerRenderer(ScenarioTableHeader.class::isInstance, renderer(new ScenarioTableHeaderRenderer()));
        group.registerRenderer(JavaSource.class::isInstance, renderer(new JavaSourceRenderer()));
        group.registerRenderer(Notes.class::isInstance, renderer(new NotesRenderer()));
        group.registerRenderer(LinkingNote.class::isInstance, renderer(new LinkingNoteRenderer(result.getTestClass())));
        group.registerRenderer(ContentAtUrl.class::isInstance, renderer(Object::toString));
        customRenderers.forEach(predicateRendererPair ->
                group.registerRenderer(o -> predicateRendererPair.getKey().test(o), renderer(predicateRendererPair.getValue())));

        Optional<Class<?>> optionalDocument = Creator.optionalClass("org.jdom.Document");
        if (optionalDocument.isPresent()) {
            group.registerRenderer(Document.class::isInstance, renderer(Creator.<Renderer>create(Class.forName("com.googlecode.yatspec.plugin.jdom.DocumentRenderer"))));
        }

        final StringTemplate template = group.getInstanceOf("yatspec");
        template.setAttribute("cssClass", getCssMap());
        template.setAttribute("testResult", result);
        StringWriter writer = new StringWriter();
        template.write(new NoIndentWriter(writer));
        return writer.toString();
    }

    public <T> HtmlResultRenderer withCustomRenderer(Class<T> klazz, Renderer<T> renderer) {
        return withCustomRenderer(klazz::isInstance, renderer);
    }

    private <T> HtmlResultRenderer withCustomRenderer(Predicate<T> predicate, Renderer<T> renderer) {
        customRenderers.add(new SimpleEntry<>(predicate, renderer));
        return this;
    }

    public <T> com.googlecode.funclate.Renderer<? super T> renderer(final Renderer<T> value) {
        return value::render;
    }

    public static Content loadContent(final String resource) {
        return new ContentAtUrl(HtmlResultRenderer.class.getResource(resource));
    }

    public static Map<Status, String> getCssMap() {
        return new HashMap<>() {{
            put(Status.Passed, "test-passed icon-check");
            put(Status.Failed, "test-failed icon-times");
            put(Status.NotRun, "test-not-run icon-warning");
        }};
    }

    public static String htmlResultRelativePath(Class<?> resultClass) {
        return FilesUtil.toPath(resultClass) + ".html";
    }

    private static File htmlResultFile(File outputDirectory, Class<?> resultClass) {
        return new File(outputDirectory, htmlResultRelativePath(resultClass));
    }

    public static String testMethodRelativePath(TestMethod testMethod) {
        return format("%s#%s",
                htmlResultRelativePath(testMethod.getTestClass()),
                testMethod.getName());
    }

    private void addAdjacentFile(File htmlResultFile, String fileName) throws IOException {
        File resultDirectory = htmlResultFile.getParentFile();
        File outputFile = new File(resultDirectory, fileName);
        Files.writeString(outputFile.toPath(), loadContent(fileName).toString());
    }
}
