package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.*;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;
import static java.util.stream.Collectors.toList;

public class IndexModel {
    private final Sequence<Result> entries;
    private final Sequence<String> packageNames;

    public IndexModel(Index index) {
        this.entries = index.entries().memorise();
        this.packageNames = entries.map(Result::getPackageName).unique();
    }

    public Model asModel() {
        return model().add("packages", packageNames.stream()
                .map(this::modelOfPackage)
                .collect(toList()));
    }

    private Model modelOfPackage(String name) {
        return model()
                .add("name", name)
                .add("status", statusOfPackage(name))
                .add("results", entries.stream()
                        .filter(result -> result.getPackageName().equalsIgnoreCase(name))
                        .map(this::modelOfResult)
                        .collect(toList()));
    }

    private Model modelOfResult(Result result) {
        return model()
                .add("name", result.getName())
                .add("url", htmlResultRelativePath(result.getTestClass()))
                .add("status", some(result).map(Results.resultStatus()).get())
                .add("methods", result.getTestMethods().stream()
                        .map(this::testMethodModel)
                        .collect(toList()));
    }

    private Status statusOfPackage(String name) {
        return entries.
                filter(where(Results.packageName(), startsWith(name))).
                map(Results.resultStatus()).
                sortBy(StatusPriority.statusPriority()).
                headOption().
                getOrElse(Status.Passed);
    }

    private Model testMethodModel(TestMethod testMethod) {
        return model()
                .add("name", testMethod.getName())
                .add("url", testMethodRelativePath(testMethod))
                .add("status", testMethod.getStatus());
    }
}
