package com.googlecode.yatspec.fixture;

import java.util.Collection;
import java.util.Random;

public class RandomFixtures {

    private static final Random random = new Random();

    public static String anyString() {
        return anyStringOfLength(anyNumberBetween(8, 12));
    }

    private static String anyStringOfLength(int length) {
        String anyString = "";
        for (int index = 0; index < length; index++) {
            anyString += anyCapitalLetter();
        }
        return anyString;
    }

    private static String anyCapitalLetter() {
        return String.valueOf((char) (random.nextInt(26) + 'A'));
    }

    public static int anyNumberBetween(int inclusiveStart, int inclusiveEnd) {
        return random.nextInt(inclusiveEnd - inclusiveStart + 1) + inclusiveStart;
    }

    public static <T> T pickOneOf(T... choices) {
        return choices[random.nextInt(choices.length)];
    }

}
