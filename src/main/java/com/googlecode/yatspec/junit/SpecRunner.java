package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

/**
 * @Deprecated Moving towards JUnit 5 only support -
 * See SpecListener.java
 */
@Deprecated(forRemoval = true)
public class SpecRunner extends TableRunner {

    private final Result testResult;
    private Map<String, Scenario> currentScenario = new HashMap<>();

    public SpecRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        testResult = new TestResult(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return super.computeTestMethods().stream()
                .filter(not(isEvaluateMethod()))
                .collect(toList());
    }

    private Predicate<FrameworkMethod> isEvaluateMethod() {
        return method -> "evaluate".equals(method.getName());
    }

    private WithCustomResultListeners listeners = new DefaultResultListeners();

    @Override
    protected Object createTest() throws Exception {
        Object instance = super.createTest();
        if (instance instanceof WithCustomResultListeners) {
            listeners = (WithCustomResultListeners) instance;
        } else {
            listeners = new DefaultResultListeners();
        }
        return instance;
    }

    @Override
    public void run(RunNotifier notifier) {
        final SpecListener listener = new SpecListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);
        listeners.complete(testResult);
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Statement statement = super.methodInvoker(method, test);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String fullyQualifiedTestMethod = test.getClass().getCanonicalName() + "." + method.getName();
                final Scenario scenario = testResult.getScenario(method.getName());
                currentScenario.put(fullyQualifiedTestMethod, scenario);

                if (test instanceof WithTestState) {
                    TestState testState = ((WithTestState) test).testState();
                    currentScenario.get(fullyQualifiedTestMethod).setTestState(testState);
                }
                statement.evaluate();
            }
        };
    }

    private final class SpecListener extends RunListener {
        @Override
        public void testFailure(Failure failure) {
            String fullyQualifiedTestMethod = failure.getDescription().getClassName() + "." + failure.getDescription().getMethodName();
            if (currentScenario.get(fullyQualifiedTestMethod) != null)
                currentScenario.get(fullyQualifiedTestMethod).setException(failure.getException());
        }
    }
}
