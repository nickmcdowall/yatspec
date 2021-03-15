package com.googlecode.yatspec.state;

import lombok.Value;

import java.util.List;

@Value
public class ScenarioName {
    String methodName;
    List<String> row;

    public boolean hasEmptyRow() {
        return getRow().isEmpty();
    }
}
