package com.incident.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    OPEN("open"),

    IN_PROGRESS("in progress"),
    CLOSED("closed");

    public final String label;

    Status(String label) {
        this.label = label;
    }

    @JsonCreator
    public static Status fromValue(String name) {
        try {
            return Status.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
