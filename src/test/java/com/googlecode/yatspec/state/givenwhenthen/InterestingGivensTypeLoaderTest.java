package com.googlecode.yatspec.state.givenwhenthen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterestingGivensTypeLoaderTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final Class<String> CLASS = String.class;

    private InterestingGivens interestingGivens;

    @BeforeEach
    void setUp() {
        interestingGivens = new InterestingGivens();
        interestingGivens.add(KEY, VALUE);
        interestingGivens.add(VALUE);
    }

    @Test
    void shouldGetTypeByClass() throws Exception {
        assertThat(InterestingGivensTypeLoader.interestingGivensType(interestingGivens, CLASS).call(), is(VALUE));
    }

    @Test
    void throwsExceptionWhenTypeByClassIsNotFound() {
        assertThrows(IllegalStateException.class,
                () -> InterestingGivensTypeLoader.interestingGivensType(interestingGivens, Integer.class).call()
        );
    }


    @Test
    void shouldGetTypeByKeyAndClass() throws Exception {
        assertThat(InterestingGivensTypeLoader.interestingGivensType(interestingGivens, KEY, CLASS).call(), is(VALUE));
    }

    @Test
    void throwsExceptionWhenTypeByKeyAndClassIsNotFound() {
        assertThrows(ClassCastException.class,
                () -> InterestingGivensTypeLoader.interestingGivensType(interestingGivens, KEY, Integer.class).call());
    }

}
