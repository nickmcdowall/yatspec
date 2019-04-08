package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.googlecode.yatspec.junit.Notes.methods.notes;
import static com.googlecode.yatspec.parsing.TestParser.parseTestMethods;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpecListener.class)
class TestParserTest {

    private static final String A_SIMPLE_STRING = "aString";

    @Test
    @Notes("Some method notes")
    public void testParseTestMethods() throws Exception {
        final List<TestMethod> methods = parseTestMethods(getClass());
        List<Annotation> annotations = extractAnnotations(methods);
        assertThat(notes(annotations).get().value(), is("Some method notes"));
    }

    public List<Annotation> extractAnnotations(List<TestMethod> methods) {
        return methods.stream().findFirst().get().getAnnotations();
    }

    @ParameterizedTest
    @Table({
            @Row({"meh"})
    })
    void yatspecWillTrimWhitespaceLeftBehindByQDoxInTableTestAnnotationsWhenAFieldVariableIsDeclared(String something) throws Exception {
        //A workaround for weirdness in QDox
    }

    @ParameterizedTest
    @Table({
            @Row({"string with\" quotes"})
    })
    void supportsQuotationMarksInParameters(String value) {
        assertThat(value, is("string with\" quotes"));
    }

    @ParameterizedTest
    @Table({
            @Row({"string with\\ escape chars"})
    })
    void supportsEscapedCharactersInParameters(String value) {
        assertThat(value, is("string with\\ escape chars"));
    }

    @ParameterizedTest
    @Table({
            @Row(A_SIMPLE_STRING)
    })
    void shouldParseParametersDeclaredAsConstants(String param) {
        assertEquals("aString", param);
    }

}
