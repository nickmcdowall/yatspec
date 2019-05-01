package com.googlecode.yatspec.junit;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface YatspecAnnotation {
    class methods {
        public static List<Annotation> yatspecAnnotations(Collection<Annotation> annotations) {
            return annotations.stream()
                    .filter(annotation -> stream(annotation.annotationType().getDeclaredAnnotations())
                            .anyMatch(declared -> declared.annotationType().equals(YatspecAnnotation.class)))
                    .collect(toList());
        }

    }

}
