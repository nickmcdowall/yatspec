package com.googlecode.yatspec.parsing;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class TestText {
    private static final String COMMA_OR_OPENING_BRACKET = "(\\s*[\\(\\,\"]\\s*)";
    private static final String COMMA_OR_CLOSING_BRACKET = "(\\s*[\\,\\)\"]\\s*)";
    private final String value;

    public TestText(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public TestText replace(final List<String> oldValues, final List<String> newValues) {
        String result = value();
        for (int i = 0; i < oldValues.size(); i++) {
            String header = oldValues.get(i);
            String value = newValues.get(i);
            result = result.replaceAll(COMMA_OR_OPENING_BRACKET + header + COMMA_OR_CLOSING_BRACKET, "$1" + displayValue(value) + "$2");
        }
        return new TestText(result);
    }

    private static String displayValue(String value) {
        if (value.matches("[A-Z0-9]*")) {
            return value;
        }
        return "\"" + value + "\"";
    }

    @Override
    public String toString() {
        return value();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
