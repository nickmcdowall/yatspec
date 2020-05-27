package com.googlecode.yatspec.state.givenwhenthen;


/**
 * No longer required (framework searches for a TestState field in test class instance automatically.
 * This will be removed soon.
 */
@Deprecated(forRemoval = true)
public interface WithTestState {

    @Deprecated(forRemoval = true)
    TestState testState();
}
