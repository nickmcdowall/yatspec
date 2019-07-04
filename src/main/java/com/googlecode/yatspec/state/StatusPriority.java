package com.googlecode.yatspec.state;

import java.util.Comparator;
import java.util.List;

public class StatusPriority implements Comparator<Status> {
    public static StatusPriority statusPriority() {
        return new StatusPriority();
    }

    private static final List<Status> PRIORITY = List.of(
            Status.Failed,
            Status.NotRun,
            Status.Passed
    );

    @Override
    public int compare(Status left, Status right) {
        return PRIORITY.indexOf(left) - PRIORITY.indexOf(right);
    }
}
