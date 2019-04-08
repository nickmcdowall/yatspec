package com.googlecode.yatspec.state.givenwhenthen;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("unchecked")
class NiceMapTest {
    @Test
    void shouldGiveAUsefulMessageIfTheWrongClassIsRequested() {
        NiceMap map = new NiceMap("Moomin");
        try {
            map.getType("String", Integer.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Moomin"));
        }
    }

}
