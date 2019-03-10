package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;

public interface WithCustomResultListeners {
    default void complete(Result testResult) {
        try {
            for (SpecResultListener resultListener : getResultListeners()) {
                resultListener.complete(SpecRunner.outputDirectory(), testResult);
            }
        } catch (Exception e) {
            System.out.println("Error while writing yatspec output");
            e.printStackTrace(System.out);
        }
    }

    Iterable<SpecResultListener> getResultListeners() throws Exception;
}
