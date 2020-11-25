package com.googlecode.yatspec.plugin.sequencediagram;

import static java.lang.String.format;

public class SequenceDiagramMessage {
    private final String from;
    private final String to;
    private final String messageName;
    private final String messageId;

    public SequenceDiagramMessage(String from, String to, String messageName, String messageId) {
        this.from = from;
        this.to = to;
        this.messageName = messageName;
        this.messageId = messageId;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }

    public String toPlantUmlMarkup() {
        return format("%s " + determineArrowType() + " %s:<text class=sequence_diagram_clickable sequence_diagram_message_id=%s>%s</text>",
                from, to, messageId, messageName);
    }

    public String determineArrowType() {
        return messageId.contains("response") ? ArrowType.DOTTED.getMarkup() : ArrowType.SOLID.getMarkup();
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequenceDiagramMessage that = (SequenceDiagramMessage) o;

        if (!from.equals(that.from)) return false;
        if (!messageId.equals(that.messageId)) return false;
        if (!messageName.equals(that.messageName)) return false;
        return to.equals(that.to);
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + messageName.hashCode();
        result = 31 * result + messageId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SequenceDiagramMessage{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", messageName='" + messageName + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
