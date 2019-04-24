package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.googlecode.yatspec.junit.WithCustomResultListeners.DEFAULT_DIR_PROPERTY;
import static com.googlecode.yatspec.junit.WithCustomResultListeners.OUTPUT_DIR_PROPERTY;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static com.googlecode.yatspec.sequence.Participants.ACTOR;
import static java.lang.System.getProperty;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith({SpecListener.class, SequenceDiagramExtension.class})
class SequenceDiagramingExampleTest implements WithTestState, WithParticipants {

    private final TestState interactions = new TestState();
    private final InterestingGivens interestingGivens = interactions.interestingGivens();

    @Test
    void bambamGetsFoodForHisDad() {
        givenMrFlintstoneIs("hungry");
        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("a burger");

        whenBarneyGivesToBambam("1 burger");
        thenBambamGivesToMrFlintstone("1 burger");
        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim();
    }

    @Table({
            @Row({"starving"}),
            @Row({"peckish"})
    })
    @ParameterizedTest
    void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleScenariosPerTestMethod(String feeling) {
        givenMrFlintstoneIs(feeling);
        whenHeDemandsFoodFromBambam();
        thenBambamPlacesABurgerOrderWithBarney("a burger");

        whenBarneyGivesToBambam("1 burger");
        thenBambamGivesToMrFlintstone("1 burger");

        thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim();
    }

    private void thenMrFlintstoneSharesHisFoodWithBarneyBecauseHeLikesHim() {
        interactions.log("(kindness) bite of burger from mrflintstone to barney", "have some of my burger because I like you");
    }

    private void thenBambamGivesToMrFlintstone(String foodItem) {
        interactions.log("(kindness) response from bambam to mrflintstone", "here is your " + foodItem);
    }

    private void whenBarneyGivesToBambam(String foodItem) {
        interactions.log("(kindness) food response from barney to bambam", foodItem + " here u go");
    }

    private void thenBambamPlacesABurgerOrderWithBarney(String foodItem) {
        interactions.log("food order from bambam to barney", "Get me " + foodItem);
    }

    private void whenHeDemandsFoodFromBambam() {
        interactions.log("food demand from mrflintstone to bambam", "I want a burger");
    }

    private void givenMrFlintstoneIs(String feeling) {
        interestingGivens.add("Flintstone is " + feeling);
    }

    @Override
    public TestState testState() {
        return interactions;
    }

    @Override
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("mrflintstone", "Mr (greedy) Flintstone"),
                ACTOR.create("bambam"),
                ACTOR.create("barney")
        );
    }

    static Path getHtmlPathFor(Class aClass) {
        String basePath = getProperty(OUTPUT_DIR_PROPERTY, getProperty(DEFAULT_DIR_PROPERTY));
        return Paths.get(basePath, htmlResultRelativePath(aClass));
    }

    static String loadResource(String name) throws IOException {
        InputStream resource = SequenceDiagramingExampleTest.class.getResourceAsStream(name);
        return new String(resource.readAllBytes());
    }

    @AfterAll
    public static void verifySequenceHtml() throws IOException {
        String expectedHtml = loadResource("SequenceDiagramingExampleTest.html");

        String generatedHtml = Files.readString(getHtmlPathFor(SequenceDiagramingExampleTest.class), UTF_8);

        assertThat(generatedHtml).isEqualToIgnoringWhitespace(expectedHtml);
    }
}
