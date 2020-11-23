package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Index {
    private final List<Result> results = new CopyOnWriteArrayList<>();

    public Index add(Result result) {
        results.add(result);
        return this;
    }

    public Collection<Result> entries() {
        return results;
    }

    public void reset() {
        results.clear();
    }
}
