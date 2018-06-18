package com.sample.api.event.seats.converters;

import com.sample.api.event.seats.model.AttendeeDto;
import com.sample.api.event.seats.resource.entity.AttendeeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to convert a attendee entity to an dto
 */
@Component
public class AttendeeEntityToDtoConverter {
    private ModelMapper modelMapper;

    @Autowired
    public AttendeeEntityToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts a attendee entity to a dto
     *
     * @param attendee    the attendee entity
     * @param attendeeDto the attendee dto
     */
    public void convert(AttendeeEntity attendee, AttendeeDto attendeeDto) {
        //map the attendee entity to the attendee dto
        modelMapper.map(attendee, attendeeDto);
    }
}
