package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.Renderer;

import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Strings.EMPTY;
import static com.googlecode.yatspec.rendering.html.JavaSourceRenderer.lines;
import static java.lang.System.lineSeparator;

public class JavaSourceRenderer implements Renderer<JavaSource> {
    @Override
    public String render(JavaSource javaSource) {
        return removeBlankLines(removeIndentation(javaSource.value()));
    }

    private String removeBlankLines(String value) {
        return value.replaceAll("^\\s+|\\s+$", EMPTY);
    }

    private String removeIndentation(String value) {
        Sequence<String> lines = lines(value);
        String indentation = lines.
                find(not(blankLine())).
                map(indentation()).getOrElse(EMPTY);
        return lines.map(remove(indentation)).toString(lineSeparator());
    }

    private Callable1<String, String> remove(final String indentation) {
        return line -> line.replaceFirst(indentation, EMPTY);
    }

    private Callable1<String, String> indentation() {
        return line -> Regex.regex("^\\s*").match(line).group();
    }

    private Predicate<String> blankLine() {
        return String::isBlank;
    }
}
