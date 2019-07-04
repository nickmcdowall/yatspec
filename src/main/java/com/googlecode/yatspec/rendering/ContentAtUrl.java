package com.googlecode.yatspec.rendering;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ContentAtUrl implements Content {

    private static final String BEGINNING_OF_STREAM = "\\A";
    private static final String BLANK = "";

    private final URL url;

    public ContentAtUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        try (Scanner scanner = new Scanner(url.openStream(), UTF_8)) {
            scanner.useDelimiter(BEGINNING_OF_STREAM);
            return scanner.hasNext() ? scanner.next() : BLANK;
        } catch (IOException e) {
            return e.toString();
        }
    }

}
