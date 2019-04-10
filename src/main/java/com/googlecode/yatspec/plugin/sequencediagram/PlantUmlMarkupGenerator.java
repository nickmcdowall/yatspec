package com.googlecode.yatspec.plugin.sequencediagram;

import java.util.Collection;
import java.util.function.Consumer;

import static java.lang.String.format;

public class PlantUmlMarkupGenerator {
    public static final String DEFAULT_SKIN = "skin BlueModern";

    public String generateMarkup(Collection<SequenceDiagramMessage> messages) {
        Markup markup = new Markup();
        messages.stream()
                .map(SequenceDiagramMessage::toPlantUmlMarkup)
                .forEach(addTo(markup));

        return markup.build();
    }

    private Consumer<String> addTo(final Markup markup) {
        return s -> markup.addMessage(s);
    }

    private class Markup {
        private GroupHelper groupHelper = new GroupHelper();
        StringBuffer plantUmlMarkup = new StringBuffer(format("@startuml%n" + DEFAULT_SKIN + "%n"));

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