package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.sequence.Participant;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class PlantUmlMarkupGenerator {

    private final PebbleEngine engine = new PebbleEngine.Builder()
            .autoEscaping(false)
            .build();

    private final PebbleTemplate compiledTemplate = engine.getTemplate("templates/plantUmlMarkup.peb");

    public static String generateMarkup(Collection<SequenceDiagramMessage> messages, List<Participant> participants) {
        PlantUmlMarkupGenerator markup = new PlantUmlMarkupGenerator();
        markup.includeParticipants(participants);
        markup.includeMessages(messages);
        return markup.build();
    }

    private final Map<String, Object> model = new HashMap<>();
    private final GroupHelper groupHelper = new GroupHelper();

    private void includeParticipants(List<Participant> participants) {
        model.put("participants", participants.stream()
                .map(Participant::toMarkup)
                .distinct()
                .collect(toList()));
    }

    private void includeMessages(Collection<SequenceDiagramMessage> messages) {
        List<String> interactions = messages.stream()
                .map(SequenceDiagramMessage::toPlantUmlMarkup)
                .flatMap(line -> List.of(groupHelper.markupGroup(line), line).stream())
                .collect(toList());
        interactions.add(groupHelper.cleanUpOpenGroups());
        model.put("interactions", interactions);
    }

    private String build() {
        Writer writer = new StringWriter();
        try {
            compiledTemplate.evaluate(writer, model);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
