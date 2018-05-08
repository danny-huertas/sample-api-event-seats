package com.tm.api.event.seats.domain;

import java.util.Arrays;

/**
 * This enum class is used to return a seat type
 */
public enum SeatType {
    ADULT,
    CHILD;

    /**
     * Uses a passed in value to get a seat type
     *
     * @param val seat type as a string
     * @return seat type object
     */
    public static SeatType get(String val) {
        return val != null ?
                Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(val)).findFirst()
                        .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", val))) :
                null;
    }
}