package com.googlecode.yatspec.plugin.sequencediagram;

enum ArrowType {
    SOLID("->>"),
    DOTTED("-->>");

    private final String markup;

    ArrowType(String markup) {
        this.markup = markup;
    }

    public String getMarkup() {
        return markup;
    }
}
