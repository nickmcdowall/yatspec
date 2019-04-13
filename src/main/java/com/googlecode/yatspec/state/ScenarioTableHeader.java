package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ScenarioTableHeader implements Value<String> {
    private final String header;

    public ScenarioTableHeader(String header) {
        this.header = header;
    }

    @Override
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
