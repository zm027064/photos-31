package com.example.myapplication.model;

import java.io.Serializable;

public enum TagType implements Serializable {
    PERSON("Person"),
    LOCATION("Location");

    private final String displayName;

    TagType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TagType fromString(String value) {
        for (TagType type : TagType.values()) {
            if (type.displayName.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return PERSON;
    }
}
