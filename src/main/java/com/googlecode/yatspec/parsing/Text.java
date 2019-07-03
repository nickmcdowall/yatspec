package com.googlecode.yatspec.parsing;

import static java.lang.Character.toTitleCase;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Text {

    private static final String CODE_SYNTAX = "[\\(\\)\\[\\]{}:;,]";
    private static final String SPACE = " ";

    public static String wordify(String value) {
        final String wordified = value
                .replaceAll(CODE_SYNTAX, SPACE)
                .replaceAll("([a-z])\\.([a-z])", "$1 $2")
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1 $2")
                .replaceAll("(?<!^)[\\t\\x0B\\f ]+", SPACE)
                .toLowerCase()
                .trim();

        return capitalise(wordified);
    }

    private static String capitalise(String value) {
        if (isEmpty(value)) return value;

        return stream(value.split(lineSeparator()))
                .map(Text::firstCharacterUppercase)
                .collect(joining(lineSeparator()));
    }

    private static String firstCharacterUppercase(String line) {
        return toTitleCase(line.charAt(0)) + line.substring(1);
    }

}