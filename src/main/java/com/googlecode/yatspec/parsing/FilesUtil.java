package com.googlecode.yatspec.parsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FilesUtil {

    public static void overwrite(File outputFile, String content) throws IOException {
        outputFile.delete();
        outputFile.getParentFile().mkdirs();
        Files.writeString(outputFile.toPath(), content);
        printLocationOf(outputFile);
    }

    private static void printLocationOf(File outputFile) {
        System.out.println("Yatspec outputFile:\nfile://" + outputFile);
    }

    public static String toJavaResourcePath(Class<?> testClass) {
        return toResourcePath(testClass) + ".java";
    }

    public static String toJavaPath(Class<?> testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toResourcePath(Class<?> clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }

    public static String toPath(Class<?> clazz) {
        return clazz.getName().replaceAll("\\.", File.separator);
    }
}
