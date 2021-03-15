package com.googlecode.yatspec.state;

import lombok.Value;

import static com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer.replaceTroublesomeCharacters;

@Value
public class LogKey {
    String value;

    public String getHtmlSafeValue() {
        return replaceTroublesomeCharacters(value, "_");
    }
}
