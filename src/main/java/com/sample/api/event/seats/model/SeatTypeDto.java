package com.sample.api.event.seats.model;

import java.util.Arrays;

/**
 * This enum class is used to return a seat type
 */
public enum SeatTypeDto {
    ADULT,
    CHILD;

    /**
     * Uses a passed in value to get a seat type
     *
     * @param val seat type as a string
     * @return seat type object
     */
    public static SeatTypeDto get(String val) {
        return val != null ?
                Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(val)).findFirst()
                        .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", val))) :
                null;
    }
}