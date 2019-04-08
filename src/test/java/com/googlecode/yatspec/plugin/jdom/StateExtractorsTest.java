package com.googlecode.yatspec.plugin.jdom;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.hamcrest.MatcherAssert;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateExtractorsTest {

    private static final String NAME = "Fred";
    private static final String ANOTHER_NAME = "Joe";
    private static final String KEY = "Key";

    @Test
    void shouldExtractValue() throws Exception {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, NAME);
        MatcherAssert.assertThat(StateExtractors.getValue(KEY, String.class).execute(inputAndOutputs), is(NAME));
    }

    @Test
    void shouldExtractValues() throws Exception {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, Arrays.asList(NAME, NAME));
        MatcherAssert.assertThat(StateExtractors.<Collection<String>, String>getValues(KEY).execute(inputAndOutputs), hasItem(NAME));
    }

    @Test
    void shouldExtractXpathElement() throws Exception {
        Document document = document("<People><Person><Name>" + NAME + "</Name></Person></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        MatcherAssert.assertThat(StateExtractors.getXpathValue(KEY, "//People/Person/Name").execute(inputAndOutputs), is(NAME));
    }

    @Test
    void shouldExtractMultipleSiblingXPathElements() throws Exception {
        Document document = document("<People><Person>" + "<Name>" + NAME + "</Name>" + "<Name>" + ANOTHER_NAME + "</Name>" + "</Person></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        MatcherAssert.assertThat(StateExtractors.getXpathValues(KEY, "//People/Person/Name").execute(inputAndOutputs), is(asList(NAME, ANOTHER_NAME)));
    }

    @Test
    void shouldExtractXpathAttribute() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        MatcherAssert.assertThat(StateExtractors.getXpathValue(KEY, "//People/Person/@Name").execute(inputAndOutputs), is(NAME));
    }

    @Test
    void shouldExtractSiblingXpathAttribute() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/><Person Name=\"" + ANOTHER_NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        MatcherAssert.assertThat(StateExtractors.getXpathValues(KEY, "//People/Person/@Name").execute(inputAndOutputs), is(asList(NAME, ANOTHER_NAME)));
    }

    @Test
    void shouldThrowExceptionWhenXPathIsNotFound() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);

        assertThrows(IllegalStateException.class, () ->
                StateExtractors.getXpathValue(KEY, "//doesNotExist").execute(inputAndOutputs)
        );
    }

    private Document document(String xml) throws JDOMException, IOException {
        return new SAXBuilder().build(new StringReader(xml));
    }

}
