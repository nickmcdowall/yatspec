package com.googlecode.yatspec.junit;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class SpecRunnerTest {
    private PrintStream standardOutStream;
    private PrintStream systemStandardOutStream;

    @Before
    public void setUp() {
        systemStandardOutStream = System.out;
        standardOutStream = new PrintStream(new ByteArrayOutputStream());
        System.setOut(standardOutStream);
    }

    @After
    public void tearDown() {
        System.setOut(systemStandardOutStream);
    }

    @Test
    public void canRunATestClassWithAnIgnoredTestWithoutReportingException() throws Exception {
        canRunTestClassWithoutException(IgnoredTest.class);
    }

    @Test
    public void canRunATestClassWithFalseAssumptionInBeforeWithoutReportingException() throws Exception {
        canRunTestClassWithoutException(AssumeFalseInBeforeTest.class);
    }

    @Test
    public void canRunATestClassWithFalseAssumptionInBeforeClassWithoutReportingException() throws Exception {
        canRunTestClassWithoutException(AssumeFalseInBeforeClassTest.class);
    }

    private void canRunTestClassWithoutException(Class<?> klass) throws org.junit.runners.model.InitializationError {
        SpecRunner specRunner = new SpecRunner(klass);

        RunNotifier notifier = new RunNotifier();
        specRunner.run(notifier);

        String standardOut = standardOutStream.toString();
        assertThat(standardOut, not(containsString("Exception")));
    }

    public static class IgnoredTest {
        @Test
        @Ignore
        public void ignored() {

        }
    }

    public static class AssumeFalseInBeforeTest {
        @Before
        public void setUp() {
            Assume.assumeTrue(false);
        }

        @Test
        public void ignored() {
            throw new RuntimeException("");
        }
    }

    public static class AssumeFalseInBeforeClassTest {
        @BeforeClass
        public static void setUp() {
            Assume.assumeTrue(false);
        }

        @Test
        public void ignored() {
            throw new RuntimeException("");
        }
    }
}
