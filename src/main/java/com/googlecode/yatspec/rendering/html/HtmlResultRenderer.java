package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.FilesUtil;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.jdom.DocumentRenderer;
import com.googlecode.yatspec.rendering.*;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.ScenarioTableHeader;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.jdom.Document;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.yatspec.parsing.FilesUtil.overwrite;
import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class HtmlResultRenderer implements SpecResultListener {

    private final Map<Class, Function<Result, Renderer>> typeRenderers = new HashMap<>(Map.of(
            ScenarioTableHeader.class, result -> new ScenarioTableHeaderRenderer(),
            JavaSource.class, result -> new JavaSourceRenderer(),
            Notes.class, result -> new NotesRenderer(),
            Document.class, result -> new DocumentRenderer(),
            LinkingNote.class, result -> new LinkingNoteRenderer(result.getTestClass())
    ));

    private final PebbleEngine engine = new PebbleEngine.Builder()
            .methodAccessValidator((object, method) -> true) //TODO look into using the default validator when possible
            .extension(additionalFilters(Map.of("render", new RenderFilter())))
            .build();

    private final PebbleTemplate compiledTemplate = engine.getTemplate("templates/yatspec.peb");

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
        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, Map.of(
                "cssClass", getCssMap(),
                "testResult", result
        ));
        return writer.toString();
    }

    public <T> HtmlResultRenderer withCustomRenderer(Class type, Function<Result, Renderer> renderer) {
        typeRenderers.put(type, renderer);
        return this;
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

    //TODO move to shared file
    public static void addAdjacentFile(File htmlResultFile, String fileName) throws IOException {
        File resultDirectory = htmlResultFile.getParentFile();
        File outputFile = new File(resultDirectory, fileName);
        Files.writeString(outputFile.toPath(), loadContent(fileName).toString());
    }

    private Extension additionalFilters(final Map<String, Filter> filterMap) {
        return new AbstractExtension() {
            @Override
            public Map<String, Filter> getFilters() {
                return filterMap;
            }
        };
    }

    // TODO move out
    private class RenderFilter implements Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException {
            if (isEmpty(input)) return null;

            Result result = (Result) args.get("result");
            return typeRenderers.entrySet().stream()
                    .filter(entry -> entry.getKey().isInstance(input))
                    .map(Map.Entry::getValue)
                    .map(rendererFromResult -> rendererFromResult.apply(result))
                    .findFirst()
                    .orElse(Object::toString)
                    .render(input);
        }

        @Override
        public List<String> getArgumentNames() {
            return List.of("result");
        }
    }
}
