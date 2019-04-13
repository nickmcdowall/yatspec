package com.googlecode.yatspec.state.givenwhenthen;

import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class TestStateTest {

    private final TestState state = new TestState();
    private final SimpleEntry<String, String> entry = new SimpleEntry<>("logTitle", "content");

    @Test
    void loggerShouldHandleNamingForMultiThreadCalls() {
        IntStream.range(0, 1000).parallel()
                .mapToObj(i -> entry)
                .forEach(pair -> state.log(pair.getKey(), pair.getValue()));

        assertThat(state.getCapturedTypes()).hasSize(1000);
    }

    @Test
    void hashCodeIsDeterministic() {
        assertThat(new TestState().hashCode())
                .isEqualTo(new TestState().hashCode());
    }
}
