package com.googlecode.yatspec.sequence;

public enum Participants {

    COLLECTIONS("collections"),
    PARTICIPANT("participant"),
    BOUNDARY("boundary"),
    CONTROL("control"),
    ENTITY("entity"),
    DATABASE("database"),
    ACTOR("actor");

    private final String type;

    Participants(final String type) {
        this.type = type;
    }

    public Participant create(final String name) {
        return new Participant() {
            @Override
            public String toMarkup() {
                return String.format("%s %s", type, name);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    public Participant create(String name, String alias) {
        return new Participant() {
            @Override
            public String toMarkup() {
                return String.format("%s %s as \"%s\"", type, name, alias);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

}
