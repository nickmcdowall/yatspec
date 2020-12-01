package com.googlecode.yatspec.parsing;


import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FilesUtilTest {

    @Test
    void convertsDotsToFileSeparator() {
        assertThat(FilesUtil.toPath(getClass()), is("com"
                + separator() + "googlecode"
                + separator() + "yatspec"
                + separator() + "parsing"
                + separator() + "FilesUtilTest")
        );
    }

    @Test
    void convertsDotsToForwardSlashes() {
        assertThat(FilesUtil.toResourcePath(getClass()), is("com/googlecode/yatspec/parsing/FilesUtilTest"));
    }

    private String separator() {
        return File.separator;
    }

}
