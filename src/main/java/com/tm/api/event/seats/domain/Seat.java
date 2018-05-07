package com.tm.api.event.seats.domain;

/**
 * Request and response object for event.
 */
public class Seat {
    private Long id;
    private Long eventId;
    private Boolean isAvailable;
    private Boolean isAisle;
    private String seatType;

    private Seat(SeatBuilder seatBuilder) {
        this.id = seatBuilder.builderSeatId;
        this.eventId = seatBuilder.builderEventId;
        this.isAvailable = seatBuilder.builderIsAvailable;
        this.isAisle = seatBuilder.builderIsAisle;
        this.seatType = seatBuilder.builderSeatType;
    }

    @Override
    public String toString() {
        return "Seat{" + "id=" + id + ", eventId=" + eventId + ", isAvailable=" + isAvailable + ", isAisle=" + isAisle
                + ", seatType=" + seatType + '}';
    }

    /**
     * SeatBuilder Object
     */
    public static class SeatBuilder {
        private Long builderSeatId;
        private Long builderEventId;
        private Boolean builderIsAvailable;
        private Boolean builderIsAisle;
        private String builderSeatType;

        public SeatBuilder seatId(Long builderSeatId) {
            this.builderSeatId = builderSeatId;
            return this;
        }

        public SeatBuilder eventId(Long builderEventId) {
            this.builderEventId = builderEventId;
            return this;
        }

        public SeatBuilder isAvailable(Boolean builderIsAvailable) {
            this.builderIsAvailable = builderIsAvailable;
            return this;
        }

        public SeatBuilder isAisle(Boolean builderIsAisle) {
            this.builderIsAisle = builderIsAisle;
            return this;
        }

        public SeatBuilder seatType(String builderSeatType) {
            this.builderSeatType = builderSeatType;
            return this;
        }

        public Seat build() {
            return new Seat(this);
        }
    }
}
