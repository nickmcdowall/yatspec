package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.sequence.Participant;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import static net.sourceforge.plantuml.FileFormat.SVG;

public class SequenceDiagramGenerator {

    public SvgWrapper generateSequenceDiagram(Collection<SequenceDiagramMessage> messages, List<Participant> participants) {
        String plantUmlMarkup = new PlantUmlMarkupGenerator().generateMarkup(messages, participants);
        return new SvgWrapper(prettyPrint(createSvg(plantUmlMarkup)));
    }

    private String prettyPrint(String xml) {
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new StringReader(xml));
            StringWriter stringWriter = new StringWriter();
            new XMLOutputter(Format.getPrettyFormat()).output(doc, stringWriter);
            return stringWriter.toString();
        } catch (JDOMException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createSvg(String plantUmlMarkup) {
        SourceStringReader reader = new SourceStringReader(plantUmlMarkup);
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            reader.outputImage(os, new FileFormatOption(SVG, false));
            return new String(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * No longer required (automatically added with HTML renderer) - method will be removed
     */
    @Deprecated(forRemoval = true)
    public static Content getHeaderContentForModalWindows() {
        return new Content() {
            @Override
            public String toString() {
                return "";
            }
        };
    }
}