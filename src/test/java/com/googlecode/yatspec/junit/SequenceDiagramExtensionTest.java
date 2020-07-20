package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class SequenceDiagramExtensionTest {

    public static final String EXCEPTION_MESSAGE = "Invalid TestState - field is missing or null. A TestState field is required to build a sequence diagram";

    private final SequenceDiagramExtension sequenceDiagramExtension = new SequenceDiagramExtension();

    @Test
    void noExceptionWhenTestStateIsValid() {
        sequenceDiagramExtension.postProcessTestInstance(new ClassWithTestState(), null);
    }

    @Test
    void noExceptionWhenTestStateIsFoundInSuperClass() {
        sequenceDiagramExtension.postProcessTestInstance(new ClassWithNestedTestState(), null);
    }

    @Test
    void throwsAnExceptionIfNoTestStateFieldExistsInTest() {
        throwsIllegalStateException(new ClassWithNoTestState());
    }

    @Test
    void throwsAnExceptionIfTestStateFieldIsNullInTest() {
        throwsIllegalStateException(new ClassWithNullTestState());
    }

    private void throwsIllegalStateException(Object testInstance) {
        try {
            sequenceDiagramExtension.postProcessTestInstance(testInstance, null);
            fail("Should throw an IllegalStateException due to missing TestState field.");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo(EXCEPTION_MESSAGE);
        }
    }

    public static class ClassWithNullTestState {
        @SuppressWarnings("unused")
        private TestState testState;
    }

    public static class ClassWithNoTestState {
    }

    public static class ClassWithTestState {
        @SuppressWarnings("unused")
        private final TestState testState = new TestState();
    }

    public static class ClassWithNestedTestState extends AnotherTestClass { }
    public static class AnotherTestClass extends ClassWithTestState { }
}