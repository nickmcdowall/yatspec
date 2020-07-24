package com.googlecode.yatspec.state.givenwhenthen;

import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class TestStateTest {

    private final TestState state = new TestState();
    private final SimpleEntry<String, String> entry = new SimpleEntry<>("logTitle", "content");

    @Test
    void prefixMessageNameWithCountWhenDuplicateKeyIsFoundToAllowForMultipleCallsBetweenSourceAndDestination() {
        state.log("message from A to B", "X");
        state.log("message from A to B", "Y");
        state.log("message from A to B", "Z");

        assertThat(state.sequenceMessages()).containsExactly(
                new SequenceDiagramMessage("A", "B", "message", "message_from_A_to_B"),
                new SequenceDiagramMessage("A", "B", "2 message", "2_message_from_A_to_B"),
                new SequenceDiagramMessage("A", "B", "3 message", "3_message_from_A_to_B")
        );
    }

    @Test
    void loggerShouldHandleNamingForMultiThreadCalls() {
        IntStream.range(0, 1000).parallel()
                .mapToObj(i -> entry)
                .forEach(pair -> state.log(pair.getKey(), pair.getValue()));

        assertThat(state.getCapturedTypes()).hasSize(1000);
    }

    @Test
    void hashCodeIsDeterministic() {
        assertThat(state.hashCode())
                .isEqualTo(new TestState().hashCode())
                .isEqualTo(new TestState(state).hashCode());
    }

    @Test
    void createCopyOfState() {
        state.interestingGivens().add("A", "B");
        state.log("C", "D");
        state.setDiagram(new SvgWrapper("svg"));

        TestState clone = new TestState(state);
        assertThat(clone).isEqualTo(state);

        state.reset();
        assertThat(state).isNotEqualTo(clone);
    }

    @Test
    void resetsFields() {
        state.interestingGivens().add("A", "B");
        state.log("C", "D");
        state.setDiagram(new SvgWrapper("svg"));

        state.reset();

        assertThat(state.getDiagram()).isNull();
        assertThat(state.interestingGivens().getTypes()).isEmpty();
        assertThat(state.getCapturedTypes()).isEmpty();
        assertThat(state.sequenceMessages()).isEmpty();
    }

    @Test
    void uniqueCorrelationIdGenerationPerThread() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> correlationId1 = executorService.submit(state::getCorrelationId);
        Future<String> correlationId2 = executorService.submit(state::getCorrelationId);
        assertThat(correlationId1.get()).isNotEqualTo(correlationId2.get());
    }

    @Test
    void sameCorrelationIdWithinSingleThread() {
        assertThat(state.getCorrelationId()).isEqualTo(state.getCorrelationId());
    }

    @Test
    void correlationIdHasSpecificLength() {
        assertThat(state.getCorrelationId().length()).isEqualTo(16);
    }
}
