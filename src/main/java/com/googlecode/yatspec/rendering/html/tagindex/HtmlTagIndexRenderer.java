package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.FilesUtil;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.TestMethod;
import org.apache.commons.lang3.tuple.Pair;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.*;
import static java.util.Comparator.comparing;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class HtmlTagIndexRenderer implements SpecResultListener {
    private static final String TAG_NAME = "tag";
    protected static final Index index = new Index();
    
    private final TagFinder tagFinder;
    private final JtwigTemplate template = JtwigTemplate.classpathTemplate("/tagindex_index.twig");
    private final JtwigModel model = JtwigModel.newModel();

    public HtmlTagIndexRenderer() {
        this(new NotesTagFinder());
    }

    private HtmlTagIndexRenderer(TagFinder tagFinder) {
        this.tagFinder = tagFinder;
    }

    @Override
    public void complete(File outputDir, Result result) throws IOException {
        index.add(result);
        File outputFile = outputFile(outputDir);
        FilesUtil.overwrite(outputFile, render(index));
        addAdjacentFile(outputFile, "yatspec_alt.css");
    }

    public String render(Index index) {
        return template.render(model
                .with("cssClass", getCssMap())
                .with("tags", tagModels(index)));
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
