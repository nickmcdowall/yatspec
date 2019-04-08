package com.googlecode.yatspec.state;


import org.junit.jupiter.api.Test;

import static com.googlecode.yatspec.state.StatusPriority.statusPriority;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class StatusPriorityTest {

    @Test
    void ordersStatusesAccordingToImportance() {
        assertThat(statusPriority().compare(Status.Passed, Status.Passed), is(0));

        assertThat(statusPriority().compare(Status.Passed, Status.Failed), greaterThan(0));
        assertThat(statusPriority().compare(Status.Passed, Status.NotRun), greaterThan(0));

        assertThat(statusPriority().compare(Status.Failed, Status.Passed), lessThan(0));
        assertThat(statusPriority().compare(Status.Failed, Status.NotRun), lessThan(0));

        assertThat(statusPriority().compare(Status.NotRun, Status.Passed), lessThan(0));
        assertThat(statusPriority().compare(Status.NotRun, Status.Failed), greaterThan(0));
    }
}
