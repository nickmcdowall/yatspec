package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;

import java.util.Collection;

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

    Collection<SpecResultListener> getResultListeners() throws Exception;
}
