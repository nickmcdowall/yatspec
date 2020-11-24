package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.*;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.HyperlinkRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.tagindex.HtmlTagIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.Action;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.Double.valueOf;
import static java.lang.Math.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpecListener.class)
@Notes("This is a note on the whole class\n" +
        "It will preserve space")
public class SpecificationExampleTest implements WithCustomResultListeners {
    private static final String RADICAND = "Radicand";
    private static final String RESULT = "Result";
    private static final String REGEX_PATTERN = "(?:#)([^\\s]+)";
    private static final String REPLACEMENT_PATTERN = "<a href='http://localhost:8080/pretend-issue-tracking/$1'>$1</a>";

    private final TestState interactions = new TestState();
    private final InterestingGivens interestingGivens = interactions.interestingGivens();

    @Test
    @Notes("#tag-one")
    void reallySimpleExample() {
        assertThat(sqrt(9), is(3.0));
    }

    @ParameterizedTest
    @Table({@Row({"9", "3.0"}),
            @Row({"16", "4.0"})})
    @Notes("#tag-one\n" +
            "#tag-two\n" +
            "This example combines table / row tests with specification and given when then")
    void takeTheSquareRoot(String radicand, String result) {
        given(theRadicand(radicand));
        when(weTakeTheSquareRoot());
        then(theResult(), is(valueOf(result)));
    }

    @Test
    @LinkingNote(message = "The details of how the Linking Note works can be seen in the %s", links = {LinkingNoteRendererTest.class})
    void testWithALinkingNote() {
    }

    @Test
    void printEmptyTestName() {
    }

    @ParameterizedTest
    @Table({
            @Row({"someParam", "varargA"}),
            @Row({"someParam", "varargA", "varargB", "varargC"}),
            @Row({"anotherParam"})
    })
    void callMethodsWithTrailingVarargs(String firstParam, String... otherParams) {
        assertThat(firstParam, not(isIn(otherParams)));
    }

    private Action theRadicand(final String number) {
        return () -> interestingGivens.add(RADICAND, Integer.valueOf(number));
    }

    private Action weTakeTheSquareRoot() {
        return () -> {
            int number = interestingGivens.getType(RADICAND, Integer.class);
            interactions.log(RESULT, sqrt(number));
        };
    }

    private Supplier<Double> theResult() {
        return () -> interactions.getType(RESULT, Double.class);
    }

    private void then(Supplier<Double> extractor, Matcher<Double> matcher) {
        assertThat(extractor.get(), matcher);
    }

    private void when(Action action) {
        action.execute();
    }

    private void given(Action action) {
        action.execute();
    }

    public Collection<SpecResultListener> getResultListeners() {
        return List.of(
                new HtmlResultRenderer()
                        .withCustomRenderer(Notes.class, new HyperlinkRenderer(new NotesRenderer(), REGEX_PATTERN, REPLACEMENT_PATTERN)),
                new HtmlIndexRenderer(),
                new HtmlTagIndexRenderer()
        );
    }

}
