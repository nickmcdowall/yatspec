package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;

import java.util.Collection;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;

public class Results {
    public static Callable1<Result, Collection<TestMethod>> testMethods() {
        return result -> result.getTestMethods();
    }

    public static Callable1<Result, String> packageName() {
        return result -> result.getPackageName();
    }

    public static Callable1<Result, Status> resultStatus() {
        return result -> sequence(result.getTestMethods()).
                map(TestMethod::getStatus).
                sortBy(statusPriority()).
                headOption().getOrElse(Status.Passed);
    }
}
