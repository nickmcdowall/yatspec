package com.googlecode.yatspec.plugin.dictionary;

import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;

@ExtendWith(SpecListener.class)
class ExampleDictionaryTest extends TestState implements WithCustomResultListeners {

    @Test
    void theDictionaryWordsAreHighlightedTest() {
        theWordYatspecWillBeHighlightedAndHoveringWillShowTheDefinition();
    }

    private void theWordYatspecWillBeHighlightedAndHoveringWillShowTheDefinition() {
        // just for the wording
    }

    @Override
    public Collection<SpecResultListener> getResultListeners() {
        return List.of(
                new HtmlResultRenderer()
                        .withCustomScripts(new DictionaryJavaScript())
                        .withCustomHeaderContent(new DictionaryCss())
        );
    }
}
