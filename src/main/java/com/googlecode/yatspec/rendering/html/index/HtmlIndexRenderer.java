package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements SpecResultListener {
    public final static Index INDEX = new Index();

    private final JtwigTemplate template = JtwigTemplate.classpathTemplate("/index.twig");
    private final JtwigModel model = JtwigModel.newModel();

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

    private String render(Index index) {
        return template.render(model
                .with("cssClass", getCssMap())
                .with("result", new IndexModel(index).asModel()));
    }

    private String read(String name) {
        return new ContentAtUrl(getClass().getResource(name)).toString();
    }
}
