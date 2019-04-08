package com.googlecode.yatspec.parsing;


import org.junit.jupiter.api.Test;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Strings.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FilesTest {

    private static final String LONG_NAME = repeat('A').take(101).toString(EMPTY, EMPTY, EMPTY);

    @Test
    void supportsLongFileNames() {
        assertThat(Files.replaceDotsWithSlashes(LONG_NAME).length(), is(LONG_NAME.length()));
    }

}
