package com.googlecode.yatspec.parsing;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TextTest {

    @Test
    void insertsASingleSpacesBetweenCapitalsAndTrims() {
        assertThat(Text.wordify("replaceAWithB;"), is("Replace a with b"));
        assertThat(Text.wordify("doesNotBlow"), is("Does not blow"));
    }

    @Test
    void wordifyShouldNotRemoveLineBreaks() {
        assertThat(Text.wordify("Foo\nBar"), is("Foo\nBar"));
    }

    @Test
    void insertsSpaceOnFullStops() {
        assertThat(Text.wordify("something.and(then.perhaps, something.else)"), is("Something and then perhaps something else"));
    }

    @Test
    void doesNotInsertSpaceOrRemoveDecimalPointForFloats() {
        assertThat(Text.wordify("assertThat(sqrt(421.0704), is(20.520));"), is("Assert that sqrt 421.0704 is 20.520"));
        assertThat(Text.wordify("13.37 66.6 3.33"), is("13.37 66.6 3.33"));
        assertThat(Text.wordify("someMethod(12.5,99, 22.22"), is("Some method 12.5 99 22.22"));
    }

    @Test
    void doesNotRemoveMinusSignForNumbers() {
        assertThat(Text.wordify("-13.37"), is("-13.37"));
        assertThat(Text.wordify("-0"), is("-0"));
        assertThat(Text.wordify("-1"), is("-1"));
    }
}
