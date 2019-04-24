package com.googlecode.yatspec.sequence;

@SuppressWarnings("unused")
public enum Participants {

    COLLECTIONS("collections"),
    BOUNDARY("boundary"),
    CONTROL("control"),
    ENTITY("entity"),
    DATABASE("database"),
    ACTOR("actor");

    private String type;

    Participants(final String type) {
        this.type = type;
    }

    public Participant create(final String name) {
        return () -> String.format("%s %s", type, name);
    }

    public Participant create(String name, String alias) {
        return () -> String.format("%s %s as \"%s\"", type, name, alias);
    }

}
