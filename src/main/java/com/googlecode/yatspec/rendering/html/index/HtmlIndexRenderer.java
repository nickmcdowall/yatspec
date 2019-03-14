package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements SpecResultListener {
    private final static Index index = new Index();

    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        index.add(result);
        Files.overwrite(outputFile(yatspecOutputDir), render(index));
    }

    private String render(Index index) {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.setRootDir(null); //forces use of classpath lookup
        StringTemplate template = group.getInstanceOf("index",
                model().
                        add("script", loadContent("index.js")).
                        add("stylesheet", HtmlResultRenderer.loadContent("yatspec.css")).
                        add("cssClass", getCssMap()).
                        add("result", new IndexModel(index).asModel()).toMap());
        return template.toString();
    }

    public static File outputFile(File outputDirectory) {
        return new File(outputDirectory, "index.html");
    }

    private Content loadContent(String name) {
        return new ContentAtUrl(getClass().getResource(name));
    }
}
