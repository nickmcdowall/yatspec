package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements SpecResultListener {
    private final Index index = new Index();

    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        index.add(result);
        com.googlecode.yatspec.parsing.Files.overwrite(new File(yatspecOutputDir, "index.html"), render(index));
        addFile(yatspecOutputDir, "index.css");
        addFile(yatspecOutputDir, "index.js");
    }

    private void addFile(File directory, String fileName) throws IOException {
        String targetFileContent = loadContent(fileName).toString();
        Files.write(new File(directory, fileName).toPath(), targetFileContent.getBytes());
    }

    private String render(Index index) {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.setRootDir(null); //forces use of classpath lookup
        StringTemplate template = group.getInstanceOf("index",
                model().
                        add("cssClass", getCssMap()).
                        add("result", new IndexModel(index).asModel()).toMap());
        return template.toString();
    }

    private Content loadContent(String name) {
        return new ContentAtUrl(getClass().getResource(name));
    }
}
