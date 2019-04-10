package com.googlecode.yatspec.plugin.sequencediagram;

enum ArrowType {
    REQUEST("->>"),
    RESPONSE("-->>");

    private String markup;

    ArrowType(String markup) {
        this.markup = markup;
    }

    public String getMarkup() {
        return markup;
    }
}
