package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByNamingConventionMessageProducer {
    public static final List<String> DODGY_CHARACTERS = List.of(" ", "/", "-", "=", "&", "\\.", "\\?", "\\(", "\\)");

    private static final String SEQUENCE_MESSAGE_REGEX = "(.*) from (.*) to (.*)";
    private static final Pattern SEQUENCE_MESSAGE_PATTERN = Pattern.compile(SEQUENCE_MESSAGE_REGEX);
    private static final String UNDERSCORE = "_";

    public Collection<SequenceDiagramMessage> messages(CapturedInputAndOutputs inputAndOutputs) {
        Collection<SequenceDiagramMessage> result = new ArrayList<>();
        Set<String> keys = inputAndOutputs.getTypes().keySet();
        for (String key : keys) {
            Matcher matcher = SEQUENCE_MESSAGE_PATTERN.matcher(key);
            if (matcher.matches()) {
                addSequenceDiagramMessage(result, key, matcher);
            }
        }
        return result;
    }

    private void addSequenceDiagramMessage(Collection<SequenceDiagramMessage> result, String key, Matcher matcher) {
        final String what = matcher.group(1).trim();
        final String from = matcher.group(2).trim();
        final String to = matcher.group(3).trim();
        final String messageId = replaceTroublesomeCharacters(key, UNDERSCORE);
        result.add(new SequenceDiagramMessage(from, to, what, messageId));
    }

    public static String replaceTroublesomeCharacters(String key, String replacement) {
        return DODGY_CHARACTERS.stream()
                .reduce(key, (newKey, dodgyCharacter) -> newKey.replaceAll(dodgyCharacter, replacement));
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,
                "SEQUENCE_MESSAGE_PATTERN");
    }
}
