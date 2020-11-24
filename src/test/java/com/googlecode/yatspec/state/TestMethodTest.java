package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestMethodTest {

    private final List<String> headers = List.of("firstParam", "otherParams");
    private final TestMethod testMethod = aStubbedMethod("methodName");

    @Test
    void handleVarargs() {
        List<String> result = testMethod.processValues(List.of("someParam", "varargA", "varargB", "varargC"), headers);

        assertThat(result).isEqualTo(List.of("someParam", "[varargA, varargB, varargC]"));
    }

    @Test
    void handleSingleVararg() {
        List<String> result = testMethod.processValues(List.of("someParam", "varargA"), headers);

        assertThat(result).isEqualTo(List.of("someParam", "varargA"));
    }

    @Test
    void handleNoVararg() {
        List<String> result = testMethod.processValues(List.of("someParam"), headers);

        assertThat(result).isEqualTo(List.of("someParam", "[]"));
    }

    @Test
    void handleMissingArgs() {
        List<String> result = testMethod.processValues(List.of(), headers);

        assertThat(result).isEqualTo(List.of("[]", "[]"));
    }

    private TestMethod aStubbedMethod(String methodName) {
        return new TestMethod(
                getClass(),
                getClass().getEnclosingMethod(),
                methodName,
                new JavaSource(""),
                new ScenarioTable()
        );
    }
}