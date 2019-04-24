package com.googlecode.yatspec.sequence;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipantsTest {

    @EnumSource(Participants.class)
    @ParameterizedTest
    void generatesExpectedMarkup(Participants participant) {
        String umlMarkup = participant.create("shortName", "alias").toMarkup();
        assertThat(umlMarkup).isEqualTo(participant.name().toLowerCase() + " shortName as \"alias\"");
    }

    @EnumSource(Participants.class)
    @ParameterizedTest
    void generatesExpectedMarkupWithAlias(Participants participant) {
        String umlMarkup = participant.create("givenName").toMarkup();
        assertThat(umlMarkup).isEqualTo(participant.name().toLowerCase() + " givenName");
    }
}