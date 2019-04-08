package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

public class ChildClassTest extends AbstractTestCase {

    private TestState testState = new TestState();

    @Override
    public TestState testState() {
        return testState;
    }
}
