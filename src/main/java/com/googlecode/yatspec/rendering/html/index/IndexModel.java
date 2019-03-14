package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.PackageNames;
import com.googlecode.yatspec.state.*;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;

public class IndexModel {
    private final Sequence<Result> entries;
    private final Sequence<String> packageNames;

    public IndexModel(Index index) {
        this.entries = index.entries().memorise();
        this.packageNames = entries.
                map(Result::getPackageName).
                unique().
                flatMap(PackageNames.allAncestors()).
                unique();
    }

    public Model asModel() {
        return model().add("packages", modelOfPackage("").getValues("packages"));
    }

    private Model modelOfPackage(String name) {
        return model().
                add("name", Text.wordify(PackageNames.packageDisplayName(name))).
                add("status", statusOfPackage(name)).
                add("packages", packageNames.
                        filter(PackageNames.directSubpackageOf(name)).
                        sortBy(returnArgument(String.class)).
                        map(this::modelOfPackage).
                        toList()).
                add("results", entries.
                        filter(where(Results.packageName(), is(name))).
                        map(modelOfResult()).
                        toList());
    }

    private Callable1<Result, Model> modelOfResult() {
        return result -> model().
                add("name", result.getName()).
                add("url", htmlResultRelativePath(result.getTestClass())).
                add("status", some(result).map(Results.resultStatus()).get()).
                add("methods", sequence(result.getTestMethods()).
                        map(testMethodModel()).
                        toList());
    }

    private Status statusOfPackage(String name) {
        return entries.
                filter(where(Results.packageName(), startsWith(name))).
                map(Results.resultStatus()).
                sortBy(StatusPriority.statusPriority()).
                headOption().
                getOrElse(Status.Passed);
    }

    private Callable1<TestMethod, Model> testMethodModel() {
        return testMethod -> model().
                add("name", testMethod.getDisplayName()).
                add("url", testMethodRelativePath(testMethod)).
                add("status", testMethod.getStatus());
    }

}
