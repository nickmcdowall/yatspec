package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.parsing.TestText;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class JavaSourceRenderer implements Renderer<TestText> {
    private static final Pattern DOT_CLASS = Pattern.compile("\\.class(\\W|$)");

    @Override
    public String render(TestText testText) {
        List<String> lines = lines(removeDotClass(testText.getValue().trim()));
        return lines.stream()
                .map(Text::wordify)
                .collect(joining("\n"));
    }

    public static List<String> lines(final String sourceCode) {
        return asList(sourceCode.split(lineSeparator()).clone());
    }

    public static String removeDotClass(String s) {
        return DOT_CLASS.matcher(s).replaceAll("$1");
    }
}
