package com.tm.api.event.seats.controllers.model;

import com.tm.api.common.operation.Operation;

/**
 * SeatResponseDto Object
 */
public class SeatResponseDto {
    private Long seatCount;
    private Operation operation;

    private SeatResponseDto(SeatResponseDtoBuilder seatResponseDtoBuilder) {
        this.seatCount = seatResponseDtoBuilder.builderSeatCount;
        this.operation = seatResponseDtoBuilder.builderOperation;
    }

    public Long getSeatCount() {
        return seatCount;
    }

    public Operation getOperation() {
        return operation;
    }

    /**
     * SeatResponseDtoBuilder Object
     */
    public static class SeatResponseDtoBuilder {
        private Long builderSeatCount;
        private Operation builderOperation;

        public SeatResponseDtoBuilder setSeatCount(Long builderSeatCount) {
            this.builderSeatCount = builderSeatCount;
            return this;
        }

        public SeatResponseDtoBuilder setOperation(Operation builderOperation) {
            this.builderOperation = builderOperation;
            return this;
        }

        public SeatResponseDto build() {
            return new SeatResponseDto(this);
        }
    }
}
