package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;

import java.io.File;
import java.util.Collection;

import static java.lang.System.getProperty;

public interface WithCustomResultListeners {

    String OUTPUT_DIR_PROPERTY = "yatspec.output.dir";
    String DEFAULT_DIR_PROPERTY = "java.io.tmpdir";

    default void complete(Result testResult) {
        try {
            for (SpecResultListener resultListener : getResultListeners()) {
                resultListener.complete(getYatspecOutputDir(), testResult);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while writing yatspec output", e);
        }
    }

    default File getYatspecOutputDir() {
        return new File(getProperty(OUTPUT_DIR_PROPERTY, getProperty(DEFAULT_DIR_PROPERTY)));
    }

    Collection<SpecResultListener> getResultListeners() throws Exception;
}
