package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.Result;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Index {
    private final List<Result> results = new CopyOnWriteArrayList<>();

    public Index add(Result result) {
        results.add(result);
        return this;
    }

    public Sequence<Result> entries() {
        return sequence(results);
    }

    public void reset() {
        results.clear();
    }
}
