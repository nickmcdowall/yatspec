package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.Model;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;

import java.util.Collection;
import java.util.function.Function;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;
import static com.googlecode.yatspec.state.Status.Passed;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;
import static java.util.stream.Collectors.toList;

public class IndexModel {
    private final Collection<Result> entries;
    private final Collection<String> packageNames;

    public IndexModel(Index index) {
        this.entries = index.entries();
        this.packageNames = entries.stream()
                .map(Result::getPackageName)
                .distinct()
                .collect(toList());
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
                .add("status", deriveResultStatus().apply(result))
                .add("methods", result.getTestMethods().stream()
                        .map(this::testMethodModel)
                        .collect(toList()));
    }

    private Status statusOfPackage(String name) {
        return entries.stream()
                .filter(result -> result.getPackageName().startsWith(name))
                .map(deriveResultStatus())
                .min(statusPriority())
                .orElse(Passed);
    }

    private Function<Result, Status> deriveResultStatus() {
        return result -> result.getTestMethods()
                .stream()
                .map(TestMethod::getStatus)
                .min(statusPriority())
                .orElse(Passed);
    }

    private Model testMethodModel(TestMethod testMethod) {
        return model()
                .add("name", testMethod.getName())
                .add("url", testMethodRelativePath(testMethod))
                .add("status", testMethod.getStatus());
    }
}
