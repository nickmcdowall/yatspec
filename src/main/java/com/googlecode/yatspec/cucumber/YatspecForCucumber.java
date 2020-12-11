package com.googlecode.yatspec.cucumber;

import com.googlecode.yatspec.junit.PostTestProcessing;
import com.googlecode.yatspec.junit.PreTestProcessing;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import cucumber.api.Result;
import cucumber.api.TestCase;
import cucumber.api.TestStep;
import cucumber.api.event.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static cucumber.api.Result.Type.FAILED;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class YatspecForCucumber implements EventListener {
    private final CucumberResult cucumberResult = new CucumberResult(this.getClass());
    private final List<TestCaseFinished> testCaseFinishedEvents = new ArrayList<>();
    private final String additionalProcessingBeanName;
    private String currentTestScenarioName;

    /**
     * This bean instance will be scanned for @PostTestProcessor and @PreTestProcessor
     * annotations and those annotated methods will be invoked (pre or post test run).
     * <p>
     * If you don't require any pre or post processing then use the no-args constructor instead.
     *
     * @param additionalProcessingBeanName The name of the bean within the application context
     */
    public YatspecForCucumber(String additionalProcessingBeanName) {
        this.additionalProcessingBeanName = additionalProcessingBeanName;
    }

    public YatspecForCucumber() {
        this.additionalProcessingBeanName = "";
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class,
                this::handleTestCaseStarted);

        publisher.registerHandlerFor(TestCaseFinished.class, event ->
                testCaseFinishedEvents.add(event));

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            finishProcessingCompletedScenario();
            defaultSequenceDiagramResultListener().complete(cucumberResult);
        });
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String featureName = testCase.getUri().replaceAll(".*/(.*?).feature$", "$1");
        String testScenarioName = String.format("%s - %s", featureName, testCase.getName());
        if (isVeryFirstRun()) {
            currentTestScenarioName = testScenarioName;
        } else if (!continuationOfExistingScenario(testScenarioName)) {
            finishProcessingCompletedScenario();
            prepareForNewScenario(testScenarioName);
        }
    }

    private void prepareForNewScenario(String testScenarioName) {
        invokeMethodAnnotatedWith(PreTestProcessing.class);
        testCaseFinishedEvents.clear();
        currentTestScenarioName = testScenarioName;
    }

    private void finishProcessingCompletedScenario() {
        invokeMethodAnnotatedWith(PostTestProcessing.class);
        addSequenceDiagramToTestState();
        addTestScenarioToResult(currentTestScenarioName);
    }

    private boolean continuationOfExistingScenario(String testScenarioName) {
        return testScenarioName.equals(currentTestScenarioName);
    }

    private boolean isVeryFirstRun() {
        return null == currentTestScenarioName;
    }

    private void addTestScenarioToResult(String scenarioName) {
        List<TestStep> allSteps = testCaseFinishedEvents.stream()
                .flatMap(event -> event.getTestCase().getTestSteps().stream())
                .collect(toList());
        cucumberResult.add(scenarioName, allSteps);
        Optional<Result> failedResult = testCaseFinishedEvents.stream()
                .map(testCaseFinished -> testCaseFinished.result)
                .filter(result -> result.is(FAILED))
                .findFirst();
        Scenario yatspecScenario = cucumberResult.getScenario(scenarioName);
        failedResult.ifPresent(result -> yatspecScenario.setException(result.getError()));
        getOptionalBean(TestState.class).ifPresent(testState -> {
            yatspecScenario.copyTestState(testState);
            testState.reset();
        });
    }

    private WithCustomResultListeners defaultSequenceDiagramResultListener() {
        return () -> List.of(
                new HtmlResultRenderer().withCustomRenderer(SvgWrapper.class, result -> new DontHighlightRenderer<>())
        );
    }

    public void addSequenceDiagramToTestState() {
        getOptionalBean(TestState.class).ifPresent(testState -> {
            Collection<SequenceDiagramMessage> messages = testState.sequenceMessages();
            if (messages.isEmpty()) {
                return;
            }
            testState.setDiagram(new SequenceDiagramGenerator().generateSequenceDiagram(
                    messages, getOptionalParticipants().orElse(emptyList())));
        });
    }

    private void invokeMethodAnnotatedWith(Class annotationClass) {
        getOptionalBean(additionalProcessingBeanName).ifPresent(instance -> {
            Class<?> klass = instance.getClass();
            while (klass != Object.class) {
                stream(klass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(annotationClass))
                        .forEach(invokeMethodOn(instance));
                klass = klass.getSuperclass();
            }
        });
    }

    private Consumer<Method> invokeMethodOn(Object instance) {
        return method -> {
            try {
                method.setAccessible(true);
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    protected Optional<Object> getOptionalBean(String beanName) {
        ApplicationContext applicationContext = SpringBridge.getApplicationContext();
        if (applicationContext.containsBean(beanName)) {
            return Optional.of(applicationContext.getBean(beanName));
        }
        return Optional.empty();
    }

    private <T> Optional<T> getOptionalBean(Class<T> requiredType) {
        try {
            ApplicationContext applicationContext = SpringBridge.getApplicationContext();
            return Optional.of(applicationContext.getBean(requiredType));
        } catch (NoSuchBeanDefinitionException e) {
            return Optional.empty();
        }
    }

    private Optional<List<Participant>> getOptionalParticipants() {
        try {
            ApplicationContext applicationContext = SpringBridge.getApplicationContext();
            return Optional.of(applicationContext.getBean("participants", List.class));
        } catch (NoSuchBeanDefinitionException e) {
            return Optional.empty();
        }
    }
}