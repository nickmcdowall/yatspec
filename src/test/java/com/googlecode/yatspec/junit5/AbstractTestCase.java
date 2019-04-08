package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.lang.Math.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpecListener.class)
public abstract class AbstractTestCase implements WithTestState {
    @Test
    public void testInParentClass() {
        assertThat(sqrt(9), is(3.0));
    }

}
