package com.googlecode.yatspec.parsing;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FilesTest {

    private static final String LONG_NAME = "A".repeat(201);

    @Test
    void supportsLongFileNames() {
        String converted = Files.replaceDotsWithSlashes(LONG_NAME);
        assertThat(converted.length(), is(LONG_NAME.length()));
    }

}
