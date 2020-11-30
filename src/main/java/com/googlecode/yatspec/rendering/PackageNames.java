package com.googlecode.yatspec.rendering;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.regex.Pattern.quote;
import static java.util.stream.Collectors.toList;

public class PackageNames {

    public static Predicate<String> directSubpackageOf(final String parentPackage) {
        return packageName -> packageName.matches(format("%s\\.?[^.]+$", quote(parentPackage)));
    }

    public static String packageDisplayName(String name) {
        if ("".equals(name)) {
            return "/";
        } else if (name.lastIndexOf(".") >= 0) {
            return name.substring(name.lastIndexOf(".") + 1);
        } else {
            return name;
        }
    }

    public static Function<String, Collection<String>> allAncestors() {
        return packageName -> {
            String[] words = packageName.split("\\.");
            return IntStream.range(0, words.length)
                    .mapToObj(index -> String.join(".", Arrays.copyOfRange(words, 0, index + 1)))
                    .collect(toList());
        };
    }
}
