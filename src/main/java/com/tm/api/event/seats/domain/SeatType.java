package com.tm.api.event.seats.domain;

import java.util.Arrays;

/**
 * This enum class is used to return a seat type
 */
public enum SeatType {
    ADULT("adult"),
    CHILD("child");

    private String type;

    SeatType(String type) {
        this.type = type;
    }

    public static SeatType fromType(String type) {
        return Arrays.stream(values()).filter(value -> type.equalsIgnoreCase(value.type)).findFirst().orElse(null);

    }
}