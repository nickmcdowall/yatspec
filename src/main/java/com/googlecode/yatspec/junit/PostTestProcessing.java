package com.googlecode.yatspec.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Test that use the `SequenceDiagramExtension` to generate Yatspec LSD reports can use this annotation to flag a
 * method (can be private but must take no arguments) that should be invoked after each Test has run but before the
 * sequence diagram is generated.
 * <p>
 * This may be useful if it is necessary to add additional interactions or interesting givens to the TestState instance
 * before the sequence diagrams are generated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PostTestProcessing {

}