package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.*;

import java.util.Collection;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;
import static java.util.regex.Pattern.quote;

public class PackageNames {
    public static Predicate<String> directSubpackageOf(final String parentPackage) {
        return packageName -> {
            String pattern = format("%s\\.?[^.]+$", quote(parentPackage));
            return packageName.matches(pattern);
        };
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

    /*
    allAncestors().call("com.googlecode.yatspec");

    returns a sequence of:

    "com"
    "com.googlecode"
    "com.googlecode.yatspec"
     */
    public static Callable1<String, Collection<String>> allAncestors() {
        return new Callable1<>() {
            @Override
            public Collection<String> call(String name) {
                return sequence(name.split("\\.")).
                        fold(empty(String.class), buildPath());
            }

            private Callable2<Sequence<String>, String, Sequence<String>> buildPath() {
                return (allPaths, name) -> {
                    Option<String> longestPath = allPaths.
                            sortBy(returnArgument(String.class)).
                            lastOption();
                    if (longestPath.isEmpty())
                        return allPaths.append(name);
                    else
                        return allPaths.append(longestPath.get() + "." + name);
                };
            }
        };
    }
}
