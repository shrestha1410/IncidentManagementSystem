package com.incident.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Priority {
    HIGH("High"),

    MEDIUM("Medium"),
    LOW("Low");

    public final String label;

    Priority(String label) {
        this.label = label;
    }

    @JsonCreator
    public static Priority fromValue(String name) {
        try {
            return Priority.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
