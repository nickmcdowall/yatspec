package com.googlecode.yatspec.parsing;


import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FilesTest {

    private static final String LONG_NAME = "A".repeat(201);

    /*
     * Windows
     */
    @Test
    void convertsDotsToFileSeparator() {
        assertThat(Files.toPath(getClass()), is("com"
                + separator() + "googlecode"
                + separator() + "yatspec"
                + separator() + "parsing"
                + separator() + "FilesTest")
        );
    }

    @Test
    void convertsDotsToForwardSlashes() {
        assertThat(Files.toResourcePath(getClass()), is("com/googlecode/yatspec/parsing/FilesTest"));
    }

    private String separator() {
        return File.separator;
    }

}
