package com.googlecode.yatspec.state.givenwhenthen;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import org.junit.jupiter.api.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.hamcrest.MatcherAssert.assertThat;

class TestStateTest {

    @Test
    void loggerShouldHandleNamingForMultiThreadCalls() {
        final TestState state = new TestState();
        sequence(Sequences.repeat(pair("logTitle", "content")).take(1000)).mapConcurrently(log(state), newFixedThreadPool(1000)).realise();
        assertThat(state.capturedInputAndOutputs.getTypes().size(), is(1000));
    }

    private Block<Pair<String, String>> log(final TestState state) {
        return new Block<>() {
            @Override
            protected void execute(Pair<String, String> pair) {
                state.log(pair.first(), pair.second());
            }
        };
    }

}
