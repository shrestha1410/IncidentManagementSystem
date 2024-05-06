package com.incident.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Enterprise {
    ENTERPRISE("Enterprise"),
    INDIVIDUAL("Individual"),
    GOVERNMENT("Government");
    public final String label;

    Enterprise(String label) {
        this.label = label;
    }

    @JsonCreator
    public static Enterprise fromValue(String name) {
        try {
            return Enterprise.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
