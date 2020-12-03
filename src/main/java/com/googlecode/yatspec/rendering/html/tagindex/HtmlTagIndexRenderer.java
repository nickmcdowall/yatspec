package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.FilesUtil;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;
import static com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer.packageUrl;
import static java.util.Comparator.comparing;
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
    public void complete(File outputDir, Result result) throws IOException {
        index.add(result);
        FilesUtil.overwrite(outputFile(outputDir), render(index));
    }

    public String render(Index index) {
        String packageUrl = packageUrl(getClass()).toString();
        StringTemplateGroup group = new StringTemplateGroup(packageUrl, packageUrl);
        group.setRootDir(null); //forces use of classpath to lookup template
        StringTemplate template = group.getInstanceOf("tagindex_index", Map.of(
                "stylesheet", HtmlResultRenderer.loadContent("yatspec_alt.css"),
                "cssClass", getCssMap(),
                "tags", tagModels(index)));

        return template.toString();
    }

    private List<Map<String, Object>> tagModels(Index index) {
        return index.entries().stream()
                .map(Result::getTestMethods)
                .flatMap(Collection::stream)
                .map(methodTags())
                .flatMap(Collection::stream)
                .collect(groupingBy(Pair::getLeft)).entrySet().stream()
                .sorted(comparingByKey())
                .map(toTagModel())
                .collect(toList());
    }

    private Function<TestMethod, List<Pair<String, TestMethod>>> methodTags() {
        return testMethod ->
                tagFinder.tags(testMethod).stream()
                        .map(tag -> Pair.of(tag, testMethod))
                        .collect(toList());
    }

    private static Function<Map.Entry<String, List<Pair<String, TestMethod>>>, Map<String, Object>> toTagModel() {
        return entry -> Map.of(
                "name", entry.getKey(),
                "results", entry.getValue().stream()
                        .sorted(comparing(pair -> pair.getRight().getName()))
                        .map(tagModel())
                        .collect(toList()));
    }

    private static Function<Pair<String, TestMethod>, Map<String, Object>> tagModel() {
        return tagAndTestMethod -> {
            TestMethod testMethod = tagAndTestMethod.getRight();
            return Map.of(
                    TAG_NAME, tagAndTestMethod.getLeft(),
                    "package", testMethod.getPackageName(),
                    "resultName", testMethod.getName(),
                    "url", testMethodRelativePath(testMethod),
                    "class", getCssMap().get(testMethod.getStatus()),
                    "name", testMethod.getDisplayName());
        };
    }

    private static File outputFile(File outputDirectory) {
        return new File(outputDirectory, "tag-index.html");
    }
}
