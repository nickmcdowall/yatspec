package com.googlecode.yatspec.junit;

import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TableRunner extends BlockJUnit4ClassRunner {
    private org.junit.runner.manipulation.Filter filter;

    public TableRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        // skip
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return computeTestMethods().stream()
                .flatMap(frameworkMethod -> {
                    Optional<Table> optionalAnnotation = Optional.ofNullable(frameworkMethod.getAnnotation(Table.class));
                    return optionalAnnotation
                            .map(decorateTestMethodWithAnnotation(frameworkMethod))
                            .orElse(List.of(frameworkMethod).stream());

                }).collect(toList());
    }

    private Function<Table, Stream<FrameworkMethod>> decorateTestMethodWithAnnotation(FrameworkMethod frameworkMethod) {
        return annotation -> Arrays.stream(annotation.value())
                .map(decorateTestMethod(frameworkMethod));
    }

    private static Function<Row, FrameworkMethod> decorateTestMethod(final FrameworkMethod frameworkMethod) {
        return row -> new DecoratingFrameworkMethod(frameworkMethod, row);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (filter == null) {
            return super.computeTestMethods();
        }

        return super.computeTestMethods().stream()
                .filter(frameworkMethod -> filter.shouldRun(describeChild(frameworkMethod)))
                .collect(toList());
    }

    @Override
    public void filter(org.junit.runner.manipulation.Filter filter) throws NoTestsRemainException {
        this.filter = filter;
        if (computeTestMethods().isEmpty()) {
            throw new NoTestsRemainException();
        }
    }
}