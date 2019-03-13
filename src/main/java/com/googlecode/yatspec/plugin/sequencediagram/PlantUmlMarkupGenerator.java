package com.googlecode.yatspec.plugin.sequencediagram;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;

public class PlantUmlMarkupGenerator {
    public String generateMarkup(Collection<SequenceDiagramMessage> messages) {
        Markup markup = new Markup();
        messages.stream()
                .map(plantUmlMarkup())
                .forEach(addTo(markup));

        return markup.build();
    }

    private Consumer<String> addTo(final Markup markup) {
        return s -> markup.addMessage(s);
    }

    private Function<SequenceDiagramMessage, String> plantUmlMarkup() {
        return message -> format("%s ->> %s:<text class=sequence_diagram_clickable sequence_diagram_message_id=%s>%s</text>",
                message.from(), message.to(), message.messageId(), message.messageName());
    }

    private class Markup {
        private GroupHelper groupHelper = new GroupHelper();
        StringBuffer plantUmlMarkup = new StringBuffer(format("@startuml%n"));

        private void addMessage(String messageLine) {
            plantUmlMarkup.append(groupHelper.markupGroup(messageLine));
            plantUmlMarkup.append(format("%s%n", messageLine));
        }

        public String build() {
            plantUmlMarkup.append(groupHelper.cleanUpOpenGroups());
            plantUmlMarkup.append(format("@enduml%n"));
            return plantUmlMarkup.toString();
        }
    }
}