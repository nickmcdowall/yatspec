package com.googlecode.yatspec.plugin.sequencediagram;


import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class GroupHelperTest {
    private GroupHelper groupHelper = new GroupHelper();

    @Test
    void ignoresNoGroup() {
        String response = groupHelper.markupGroup("here ->> there:<text>basdjlakjds</text>");

        assertThat(response, is(""));
    }

    @Test
    void marksGroupStart() {
        String response = groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds</text>");

        assertThat(response, is(String.format("group theGroup%n")));
    }

    @Test
    void ignoresIfGroupStarted() {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        String response = groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds2</text>");

        assertThat(response, is(String.format("")));
    }

    @Test
    void endsGroupInSequence() {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        String response = groupHelper.markupGroup("here ->> there:basdjlakjds2");

        assertThat(response, is(String.format("end%n")));
    }

    @Test
    void cleansUpOpenGroups() {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        String response = groupHelper.cleanUpOpenGroups();

        assertThat(response, is(String.format("end%n")));
    }

    @Test
    void notCleanupWhenNoOpenGroups() {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        groupHelper.markupGroup("here ->> there:<text>basdjlakjds2</text>");
        String response = groupHelper.cleanUpOpenGroups();

        assertThat(response, is(String.format("")));
    }
}
