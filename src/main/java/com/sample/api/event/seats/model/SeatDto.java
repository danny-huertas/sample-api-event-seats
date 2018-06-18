package com.sample.api.event.seats.model;

/**
 * Domain object for a seat.
 */
public class SeatDto {
    private Long id;
    private Long eventId;
    private Boolean available;
    private Boolean aisle;
    private SeatTypeDto seatTypeDto;

    private SeatDto(SeatBuilder seatBuilder) {
        this.id = seatBuilder.builderSeatId;
        this.eventId = seatBuilder.builderEventId;
        this.available = seatBuilder.builderIsAvailable;
        this.aisle = seatBuilder.builderIsAisle;
        this.seatTypeDto = SeatTypeDto.get(seatBuilder.builderSeatType);
    }

    @Override
    public String toString() {
        return "SeatDto{" + "id=" + id + ", eventId=" + eventId + ", available=" + available + ", aisle=" + aisle
                + ", seatTypeDto=" + seatTypeDto + '}';
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

        public SeatDto build() {
            return new SeatDto(this);
        }
    }
}
