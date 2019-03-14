package com.googlecode.yatspec.junit;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.Optional;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@YatspecAnnotation
public @interface Notes {
    String value();

    class methods {
        public static Optional<Notes> notes(Collection<Annotation> annotations) {
            return annotations.stream()
                    .filter(Notes.class::isInstance)
                    .map(Notes.class::cast)
                    .findFirst();
        }
    }
}
