package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.regex.Regex;

import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.regex.Regex.regex;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Text {
    private static final Regex wordDelimiter = Regex.regex(getPattern("wordDelimiter.regex"));

    private static final Pattern spaceRemover = Pattern.compile("(?<!^)[\\t\\x0B\\f ]+"); // don't replace new lines

    private static final Regex quotedStrings = regex("\"[^\"]+\"");
    private static final Callable1<MatchResult, CharSequence> wordDelimiterReplacer = matchResult -> {
        // " $1 $2"
        return " " + lowercaseSingleLetters(filterNull(matchResult.group(1))) + " " + filterNull(matchResult.group(2)).toLowerCase();
    };

    private static String filterNull(String value) {
        return value == null ? "" : value;
    }

    private static final Callable1<CharSequence, CharSequence> wordifier = text -> wordDelimiter.findMatches(text).replace(wordDelimiterReplacer);

    private static final Callable1<MatchResult, CharSequence> doNothing = MatchResult::group;

    private static String lowercaseSingleLetters(String value) {
        return value.length() == 1 ? value.toLowerCase() : value;
    }

    public static String wordify(String value) {
        final String wordified = quotedStrings.findMatches(value).replace(wordifier, doNothing);
        return capitalise(spaceRemover.matcher(wordified).replaceAll(" ").trim());
    }

    private static String capitalise(String value) {
        if (isEmpty(value)) return value;
        return Character.toTitleCase(value.charAt(0)) + value.substring(1);
    }

    private static String getPattern(String fileName) {
        try {
            return new String(Text.class.getResourceAsStream(fileName).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}