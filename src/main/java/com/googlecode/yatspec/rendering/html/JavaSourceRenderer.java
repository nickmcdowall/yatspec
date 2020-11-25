package com.googlecode.yatspec.rendering.html;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Xml.escape;
import static java.lang.System.lineSeparator;

public class JavaSourceRenderer implements Renderer<JavaSource> {
    private static final Pattern DOT_CLASS = Pattern.compile("\\.class(\\W|$)");

    @Override
    public String render(JavaSource javaSource) {
        return escape(lines(removeDotClass(javaSource.value().trim()))
                .map(Text::wordify)
                .toString("\n"));
    }

    public static Sequence<String> lines(final String sourceCode) {
        return sequence(sourceCode.split(lineSeparator()));
    }

    public static String removeDotClass(String s) {
        return DOT_CLASS.matcher(s).replaceAll("$1");
    }
}
