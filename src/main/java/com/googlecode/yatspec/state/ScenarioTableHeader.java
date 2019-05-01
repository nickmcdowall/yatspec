package com.googlecode.yatspec.state;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ScenarioTableHeader {
    private final String header;

    public ScenarioTableHeader(String header) {
        this.header = header;
    }

    public String value() {
        return header;
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
