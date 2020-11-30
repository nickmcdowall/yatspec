package com.googlecode.yatspec.parsing;

import java.io.File;

import static com.googlecode.totallylazy.Files.write;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Files {

    public static void overwrite(File output, String content) {
        output.delete();
        output.getParentFile().mkdirs();
        write(content.getBytes(UTF_8), output);
        System.out.println("Yatspec output:\nfile://" + output);
    }

    public static String toJavaResourcePath(Class testClass) {
        return toResourcePath(testClass) + ".java";
    }

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toResourcePath(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }

    public static String toPath(Class clazz) {
        return clazz.getName().replaceAll("\\.", File.separator);
    }
}
