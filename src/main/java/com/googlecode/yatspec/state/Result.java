package com.googlecode.yatspec.state;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Result {

    List<TestMethod> getTestMethods();

    Scenario getScenario(String name) throws Exception;

    String getName();

    String getPackageName();

    default List<Annotation> getAnnotations() {
        return List.of();
    }

    String getRootDirectory();

    String getHtmlFileRelativePath();
}