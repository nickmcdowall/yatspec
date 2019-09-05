package com.googlecode.yatspec.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VarargsParameterResolverTest {

    private VarargsParameterResolver resolver = new VarargsParameterResolver();

    @MethodSource("multiArguments")
    @ParameterizedTest
    void passThroughRowParamsWhenMethodHasNoVarargs(String... rowParameters) throws NoSuchMethodException {
        Object[] parameters = resolver.resolveParameters(getRow(rowParameters), getClass(), noVarargsMethod());

        assertThat(parameters).isEqualTo(rowParameters);
    }

    private static Stream<Arguments> multiArguments() {
        return Stream.of(
                Arguments.of(stringArray("a")),
                Arguments.of(stringArray("a", "b")),
                Arguments.of(stringArray("a", "b", "c")),
                Arguments.of(stringArray("a", "b", "c", "d"))
        );
    }

    private static Object stringArray(String... input) {
        return input;
    }

    @Test
    void addEmptyArrayWhenVarargParameterNotSupplied() throws NoSuchMethodException {
        String[] rowParameter = new String[]{"a"};

        Object[] parameters = resolver.resolveParameters(getRow(rowParameter), null, varargsMethod());

        assertThat(parameters).hasSize(2);
        assertThat(parameters[1]).isInstanceOf(String[].class);
    }

    @Test
    void additionalParametersAddedToVarargArray() throws NoSuchMethodException {
        String[] rowParameter = new String[]{"a", "b", "c", "d"};

        Object[] parameters = resolver.resolveParameters(getRow(rowParameter), getClass(), varargsMethod());

        assertThat(parameters).hasSize(2);
        assertThat(parameters[1]).isInstanceOf(Object[].class);
        assertThat((String[]) parameters[1]).hasSize(3);
    }

    @Test
    void exceptionWhenRowIsMissingRequiredParameters() {
        String[] rowParameter = new String[]{};

        assertThrows(IllegalArgumentException.class,
                () -> resolver.resolveParameters(getRow(rowParameter), getClass(), varargsMethod())
        );
    }

    private Method noVarargsMethod() throws NoSuchMethodException {
        return getClass().getDeclaredMethod("aMethodWithoutVarargs", Object.class);
    }

    private Method varargsMethod() throws NoSuchMethodException {
        return getClass().getDeclaredMethod("aMethodWithVarargs", Object.class, String[].class);
    }

    private void aMethodWithVarargs(
            @SuppressWarnings("unused") Object arg,
            @SuppressWarnings("unused") String... vararg) {
        return;
    }

    private void aMethodWithoutVarargs(
            @SuppressWarnings("unused") Object arg) {
        return;
    }

    private Row getRow(final String[] value) {
        return new Row() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] value() {
                return value;
            }
        };
    }
}