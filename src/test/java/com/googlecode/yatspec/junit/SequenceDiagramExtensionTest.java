package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SequenceDiagramExtensionTest {

    public static final String EXCEPTION_MESSAGE = "Invalid TestState - field is missing or null. A TestState field is required to build a sequence diagram";

    private final SequenceDiagramExtension sequenceDiagramExtension = new SequenceDiagramExtension();
    private final ExtensionContext extensionContext = mock(ExtensionContext.class);

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

    @Test
    void allowForPostTestProcessing() {
        ClassWithPostTestHookAnnotation testInstance = new ClassWithPostTestHookAnnotation();
        when(extensionContext.getRequiredTestInstance()).thenReturn(testInstance);

        sequenceDiagramExtension.afterTestExecution(extensionContext);

        assertTrue(testInstance.testState.getType("postTestCalled", Boolean.class));
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

    public static class ClassWithNestedTestState extends AnotherTestClass {
    }

    public static class AnotherTestClass extends ClassWithTestState {
    }

    public static class ClassWithPostTestHookAnnotation {
        private final TestState testState = new TestState();

        @Test
        public void someTest() {
            assertTrue(TRUE);
        }

        @PostTestProcessing
        private void somePostProcessing() {
            testState.log("postTestCalled", true);
        }
    }
}