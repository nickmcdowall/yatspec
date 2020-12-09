package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements SpecResultListener {
    public final static Index INDEX = new Index();

    private final PebbleEngine engine = new PebbleEngine.Builder().build();
    private final PebbleTemplate compiledTemplate = engine.getTemplate("templates/index.peb");


    @Override
    public void complete(File outputDir, Result result) throws Exception {
        INDEX.add(result);
        write(outputDir, "index.html", render(INDEX));
        write(outputDir, "index.css", read("index.css"));
        write(outputDir, "index.js", read("index.js"));
    }

    private void write(File directory, String fileName, String targetFileContent) throws IOException {
        Path path = new File(directory, fileName).toPath();
        Files.write(path, targetFileContent.getBytes());
        if (fileName.endsWith(".html")) System.out.println("Yatspec output:\nfile://" + path);
    }

    private String render(Index index) throws IOException {
        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, Map.of(
                "cssClass", getCssMap(),
                "result", new IndexModel(index).asModel()
        ));

        return writer.toString();
    }

    private String read(String name) {
        return new ContentAtUrl(getClass().getResource(name)).toString();
    }
}
