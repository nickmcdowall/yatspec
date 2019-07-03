package com.googlecode.yatspec.parsing;

import static java.lang.Character.toTitleCase;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Text {

    private static final String SPACE = " ";
    private static final String SPLIT_WITH_SPACE = "$1 $2";
    private static final String CODE_SYNTAX = "[\\(\\)\\[\\]{}:;,]";
    private static final String MULTI_SPACES = "(?<!^)[\\t\\x0B\\f ]+";
    private static final String WORDS_SEPARATED_BY_DOTS = "([a-zA-Z])\\.([a-zA-Z])";
    private static final String WORDS_SEPARATED_BY_CAMEL_CASE = "([a-z])([A-Z])";
    private static final String WORDS_SEPARATED_BY_CAPITALS = "([A-Z])([A-Z][a-z])";

    public static String wordify(String value) {
        String wordified = value
                .replaceAll(CODE_SYNTAX, SPACE)
                .replaceAll(WORDS_SEPARATED_BY_DOTS, SPLIT_WITH_SPACE)
                .replaceAll(WORDS_SEPARATED_BY_CAMEL_CASE, SPLIT_WITH_SPACE)
                .replaceAll(WORDS_SEPARATED_BY_CAPITALS, SPLIT_WITH_SPACE)
                .replaceAll(MULTI_SPACES, SPACE)
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