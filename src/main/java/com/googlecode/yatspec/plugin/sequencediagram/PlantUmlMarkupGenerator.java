package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.sequence.Participant;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PlantUmlMarkupGenerator {

    public static String generateMarkup(Collection<SequenceDiagramMessage> messages, List<Participant> participants) {
        PlantUmlMarkupGenerator markup = new PlantUmlMarkupGenerator();
        markup.includeParticipants(participants);
        markup.includeMessages(messages);
        return markup.build();
    }

    private final JtwigTemplate template = JtwigTemplate.classpathTemplate("plantUmlMarkup.twig");
    private final JtwigModel model = JtwigModel.newModel();
    private final GroupHelper groupHelper = new GroupHelper();

    private void includeParticipants(List<Participant> participants) {
        model.with("participants", participants.stream()
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
        model.with("interactions", interactions);
    }

    private String build() {
        return template.render(model);
    }

}