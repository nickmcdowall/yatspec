package com.googlecode.yatspec.plugin.jdom;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import java.util.Collection;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.jdom.xpath.XPath.newInstance;

public class StateExtractors {
    private StateExtractors() {
    }

    public static <T> StateExtractor<T> getValue(final String key, final Class<T> klazz) {
        return capturedInputAndOutputs -> capturedInputAndOutputs.getType(key, klazz);
    }

    @SuppressWarnings("unchecked")
    static <I extends Collection<T>, T> StateExtractor<I> getValues(final String key) {
        return capturedInputAndOutputs -> (I) capturedInputAndOutputs.getType(key, Collection.class);
    }

    static StateExtractor<String> getXpathValue(final String key, final String xpath) {
        return capturedInputAndOutputs -> getXpathValues(capturedInputAndOutputs, key, xpath).get(0);
    }

    static StateExtractor<List<String>> getXpathValues(final String key, final String xpath) {
        return capturedInputAndOutputs -> getXpathValues(capturedInputAndOutputs, key, xpath);
    }

    private static List<String> getXpathValues(CapturedInputAndOutputs capturedInputAndOutputs, String key, String xpath) throws Exception {
        return sequence(getDocument(capturedInputAndOutputs, key)).map(toAttributeOrElementValues(xpath)).head();
    }

    private static Document getDocument(CapturedInputAndOutputs capturedInputAndOutputs, String key) throws Exception {
        return getValue(key, Document.class).execute(capturedInputAndOutputs);
    }

    @SuppressWarnings("unchecked")
    private static Callable1<Document, List<String>> toAttributeOrElementValues(final String xpath) {
        return document -> {
            List result = newInstance(xpath).selectNodes(document);
            checkThatValuesAreFound(result, xpath);
            return sequence(result).map(toAttributeOrElement()).toList();
        };
    }

    private static void checkThatValuesAreFound(List result, String xpath) {
        if (result == null || result.isEmpty()) {
            throw new IllegalStateException("Result not found at " + xpath);
        }
    }


    private static Callable1<Object, String> toAttributeOrElement() {
        return result -> result instanceof Element
                ? ((Element) result).getText()
                : ((Attribute) result).getValue();
    }
}
