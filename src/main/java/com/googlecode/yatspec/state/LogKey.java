package com.googlecode.yatspec.state;

import static com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer.replaceTroublesomeCharacters;

public class LogKey {
    private final String value;

    public LogKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getHtmlSafeValue() {
        return replaceTroublesomeCharacters(value, "_");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogKey logKey = (LogKey) o;

        return value.equals(logKey.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
