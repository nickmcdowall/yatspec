package com.googlecode.yatspec.state;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class FailureMessage {
    String value;
}
