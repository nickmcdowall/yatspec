package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.annotation.AnnotationValue;
import com.thoughtworks.qdox.model.annotation.AnnotationValueList;
import com.thoughtworks.qdox.model.annotation.EvaluatingVisitor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.googlecode.yatspec.junit.YatspecAnnotation.methods.yatspecAnnotations;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class TestMethodExtractor {
    public TestMethod toTestMethod(Class aClass, JavaMethod javaMethod, Method method) {
        final var name = javaMethod.getName();
        final var source = new TestText(javaMethod.getSourceCode());
        final var scenarioTable = getScenarioTable(javaMethod);
        final var yatspecAnnotations = yatspecAnnotations(asList(method.getAnnotations()));
        
        return new TestMethod(aClass, name, source, scenarioTable, yatspecAnnotations);
    }

    private ScenarioTable getScenarioTable(JavaMethod method) {
        ScenarioTable table = new ScenarioTable();
        table.setHeaders(getNames(method.getParameters()));

        final Collection<Annotation> rows = getRows(method);
        for (Annotation row : rows) {
            List<String> values = getRowValues(row);
            table.addRow(
                    values.stream()
                            .map(String::trim)
                            .collect(toList())
            );
        }
        return table;
    }

    private List<String> getRowValues(Annotation row) {
        final AnnotationValue value = row.getProperty("value");
        final EvaluatingVisitor annotationVisitor = new EvaluatingVisitor() {
            @Override
            protected Object getFieldReferenceValue(JavaField javaField) {
                Type type = javaField.getType();
                if (type.isA(new Type(String.class.getName()))) {
                    return javaField.getInitializationExpression().replace("\"", "");
                } else {
                    return value.getParameterValue();
                }
            }
        };

        final Object parameterValue = value.accept(annotationVisitor);

        if (parameterValue instanceof List) {
            return (List<String>) parameterValue;
        } else {
            return singletonList(parameterValue.toString());
        }
    }

    private Collection<Annotation> getRows(JavaMethod method) {
        return Arrays.stream(method.getAnnotations())
                .filter(isATable())
                .flatMap(rows())
                .collect(toList());
    }

    private Predicate<Annotation> isATable() {
        return annotation -> {
            String name = annotation.getType().getFullyQualifiedName();
            return name.equals(Table.class.getName());
        };
    }

    private Function<Annotation, Stream<Annotation>> rows() {
        return annotation -> {
            AnnotationValue value = annotation.getProperty("value");
            return ((AnnotationValueList) value).getValueList().stream();
        };
    }

    private List<String> getNames(JavaParameter[] javaParameters) {
        return Arrays.stream(javaParameters)
                .map(JavaParameter::getName)
                .collect(toList());
    }
}
