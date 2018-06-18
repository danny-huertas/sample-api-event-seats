package com.sample.api.event.seats.controllers.model;

import com.sample.api.common.operation.Operation;
import com.sample.api.event.seats.model.AttendeeDto;

import java.util.List;

/**
 * AttendeeResponseDto Object
 */
public class AttendeeResponseDto {
    private List<AttendeeDto> attendeeDtos;
    private AttendeeDto attendeeDto;
    private Operation operation;

    private AttendeeResponseDto(AttendeeResponseDtoBuilder attendeeResponseDtoBuilder) {
        this.attendeeDtos = attendeeResponseDtoBuilder.builderAttendeeDtos;
        this.attendeeDto = attendeeResponseDtoBuilder.builderAttendeeDto;
        this.operation = attendeeResponseDtoBuilder.builderOperation;
    }

    public List<AttendeeDto> getAttendeeDtos() {
        return attendeeDtos;
    }

    public AttendeeDto getAttendeeDto() {
        return attendeeDto;
    }

    public Operation getOperation() {
        return operation;
    }

    /**
     * AttendeeResponseDtoBuilder Object
     */
    public static class AttendeeResponseDtoBuilder {
        private List<AttendeeDto> builderAttendeeDtos;
        private AttendeeDto builderAttendeeDto;
        private Operation builderOperation;

        public AttendeeResponseDtoBuilder setAttendeeDtos(List<AttendeeDto> builderAttendeeDtos) {
            this.builderAttendeeDtos = builderAttendeeDtos;
            return this;
        }

        public AttendeeResponseDtoBuilder setAttendeeDto(AttendeeDto builderAttendeeDto) {
            this.builderAttendeeDto = builderAttendeeDto;
            return this;
        }

        public AttendeeResponseDtoBuilder setOperation(Operation builderOperation) {
            this.builderOperation = builderOperation;
            return this;
        }

        public AttendeeResponseDto build() {
            return new AttendeeResponseDto(this);
        }
    }
}
