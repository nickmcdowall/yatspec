package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.Optional;

import static com.googlecode.yatspec.parsing.TestParser.parseTestMethods;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpecListener.class)
class TestParserTest {

    private static final String A_SIMPLE_STRING = "aString";

    @Test
    @Notes("Some method notes")
    public void parsesNotesOnTestMethods() {
        List<String> allNotes = parseTestMethods(getClass()).stream()
                .map(TestMethod::getAnnotations)
                .map(Notes.methods::notes)
                .filter(Optional::isPresent)
                .map(notes -> notes.get().value())
                .collect(toList());

        assertThat(allNotes).isEqualTo(List.of(
                "Some method notes",
                "Some more method notes"
        ));
    }

    @Notes("Some more method notes")
    @Test
    void parsedMethodsRetainCorrectOrder() {
        List<String> methodNames = parseTestMethods(getClass()).stream()
                .map(TestMethod::getName)
                .collect(toList());

        assertThat(methodNames).isEqualTo(List.of(
                "parsesNotesOnTestMethods",
                "parsedMethodsRetainCorrectOrder",
                "supportsQuotationMarksInParameters",
                "supportsEscapedCharactersInParameters",
                "shouldParseParametersDeclaredAsConstants"
        ));
    }

    @ParameterizedTest
    @Table({
            @Row({"string with\" quotes"})
    })
    void supportsQuotationMarksInParameters(String value) {
        assertThat(value).isEqualTo("string with\" quotes");
    }

    @ParameterizedTest
    @Table({
            @Row({"string with\\ escape chars"})
    })
    void supportsEscapedCharactersInParameters(String value) {
        assertThat(value).isEqualTo("string with\\ escape chars");
    }

    @ParameterizedTest
    @Table({
            @Row(A_SIMPLE_STRING)
    })
    void shouldParseParametersDeclaredAsConstants(String param) {
        assertEquals("aString", param);
    }
}
