package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Pair;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class HtmlTagIndexRenderer implements SpecResultListener {
    private static final String TAG_NAME = "tag";
    protected final static Index index = new Index();
    private final TagFinder tagFinder;

    public HtmlTagIndexRenderer() {
        this(new NotesTagFinder());
    }

    private HtmlTagIndexRenderer(TagFinder tagFinder) {
        this.tagFinder = tagFinder;
    }

    @Override
    public void complete(File yatspecOutputDir, Result result) {
        index.add(result);
        Files.overwrite(outputFile(yatspecOutputDir), render(index));
    }

    public String render(Index index) {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.setRootDir(null); //forces use of classpath to lookup template
        StringTemplate template = group.getInstanceOf("tagindex_index",
                model().add("script", null)
                        .add("stylesheet", HtmlResultRenderer.loadContent("yatspec_alt.css"))
                        .add("cssClass", getCssMap())
                        .add("tags", tagModels(index)).toMap());
        return template.toString();
    }

    private List<Model> tagModels(Index index) {
        return index.entries().stream()
                .map(Result::getTestMethods)
                .flatMap(Collection::stream)
                .map(methodTags())
                .flatMap(Collection::stream)
                .collect(groupingBy(Pair::first)).entrySet().stream()
                .sorted(comparingByKey())
                .map(toTagModel())
                .collect(toList());
    }

    private Function<TestMethod, Collection<Pair<String, TestMethod>>> methodTags() {
        return resultFileAndTestMethod ->
                sequence(tagFinder.tags(resultFileAndTestMethod))
                        .zip(repeat(resultFileAndTestMethod));
    }

    private static Function<Pair<String, TestMethod>, Model> tagModel() {
        return tagAndTestMethod -> {
            TestMethod testMethod = tagAndTestMethod.second();
            return model().
                    add(TAG_NAME, tagAndTestMethod.first()).
                    add("package", testMethod.getPackageName()).
                    add("resultName", testMethod.getName()).
                    add("url", testMethodRelativePath(testMethod)).
                    add("class", getCssMap().get(testMethod.getStatus())).
                    add("name", testMethod.getDisplayName());
        };
    }

    private static Function<Map.Entry<String, List<Pair<String, TestMethod>>>, Model> toTagModel() {
        return entry -> model()
                .add("name", entry.getKey())
                .add("results", entry.getValue().stream()
                        .map(tagModel())
                        .collect(toList()));
    }

    private static File outputFile(File outputDirectory) {
        return new File(outputDirectory, "tag-index.html");
    }
}
