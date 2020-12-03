package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements SpecResultListener {
    public final static Index INDEX = new Index();

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
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.setRootDir(null); //forces use of classpath lookup
        StringTemplate template = group.getInstanceOf("index",
                Map.of(
                        "cssClass", getCssMap(),
                        "result", new IndexModel(index).asModel()));
        return template.toString();
    }

    private String read(String name) {
        return new ContentAtUrl(getClass().getResource(name)).toString();
    }
}
