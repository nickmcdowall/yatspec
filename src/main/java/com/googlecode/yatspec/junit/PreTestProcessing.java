package com.googlecode.yatspec.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Test that use the `SequenceDiagramExtension` to generate Yatspec LSD reports can use this annotation to flag a
 * method (can be private but must take no arguments) that should be invoked before each Test has run
 * <p>
 * This may be useful if it is necessary to set up global variables or traceIds needed by the test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PreTestProcessing {

}