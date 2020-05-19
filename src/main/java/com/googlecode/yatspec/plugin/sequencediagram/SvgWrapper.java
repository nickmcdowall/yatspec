package com.googlecode.yatspec.plugin.sequencediagram;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SvgWrapper {
    private final String svg;

    public SvgWrapper(String svg) {
        this.svg = svg;
    }

    public String toString() {
        return svg;
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

