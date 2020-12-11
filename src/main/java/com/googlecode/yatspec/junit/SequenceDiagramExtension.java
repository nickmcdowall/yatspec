package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;

public class SequenceDiagramExtension extends SpecListener implements TestInstancePostProcessor, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private SequenceDiagramGenerator sequenceDiagramGenerator;
    private Optional<TestState> interactions = Optional.empty();
    private List<Participant> participants = emptyList();


    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) {
        interactions = getOptionalTestState(testInstance);

        if (interactions.isEmpty()) {
            throw new IllegalStateException("Invalid TestState - field is missing or null. A TestState field is required to build a sequence diagram");
        }

        if (testInstance instanceof WithParticipants) {
            participants = Optional.ofNullable(((WithParticipants) testInstance).participants()).orElse(emptyList());
        }
    }


    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        additionalPostTestProcessing(extensionContext.getRequiredTestInstance());
        interactions.ifPresent(interactions ->
                interactions.setDiagram(sequenceDiagramGenerator.generateSequenceDiagram(interactions.sequenceMessages(), participants)));
    }

    private void additionalPostTestProcessing(final Object instance) {
        Class<?> klass = instance.getClass();
        while (klass != Object.class) {
            stream(klass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(PostTestProcessing.class))
                    .forEach(invokeMethodOn(instance));
            klass = klass.getSuperclass();
        }
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

    @Override
    protected WithCustomResultListeners createDefaultResultListeners() {
        return defaultSequenceDiagramResultListener();
    }

    private WithCustomResultListeners defaultSequenceDiagramResultListener() {
        return () -> List.of(
                new HtmlResultRenderer().
                        withCustomRenderer(SvgWrapper.class, result -> new DontHighlightRenderer()),
                new HtmlIndexRenderer()
        );
    }
}