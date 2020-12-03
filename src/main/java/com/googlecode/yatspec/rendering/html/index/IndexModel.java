package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

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

    public Map<String, Object> asModel() {
        return Map.of("packages", packageNames.stream()
                .map(this::modelOfPackage)
                .collect(toList()));
    }

    private Map<String, Object> modelOfPackage(String name) {
        return Map.of(
                "name", name,
                "status", statusOfPackage(name),
                "results", entries.stream()
                        .filter(result -> result.getPackageName().equalsIgnoreCase(name))
                        .map(this::modelOfResult)
                        .collect(toList()));
    }

    private Map<String, Object> modelOfResult(Result result) {
        return Map.of(
                "name", result.getName(),
                "url", htmlResultRelativePath(result.getTestClass()),
                "status", deriveResultStatus().apply(result),
                "methods", result.getTestMethods().stream()
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

    private Map<String, Object> testMethodModel(TestMethod testMethod) {
        return Map.of(
                "name", testMethod.getName(),
                "url", testMethodRelativePath(testMethod),
                "status", testMethod.getStatus());
    }
}
