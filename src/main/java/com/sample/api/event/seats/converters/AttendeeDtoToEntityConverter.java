package com.sample.api.event.seats.converters;

import com.sample.api.event.seats.model.AttendeeDto;
import com.sample.api.event.seats.resource.entity.AttendeeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to convert a attendee dto to an entity
 */
@Component
public class AttendeeDtoToEntityConverter {
    private ModelMapper modelMapper;

    @Autowired
    public AttendeeDtoToEntityConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts a attendee dto to an entity
     *
     * @param attendeeDto the attendee dto
     * @param attendee    the attendee entity
     */
    public void convert(AttendeeDto attendeeDto, AttendeeEntity attendee) {
        //map the attendee dto to the attendee entity
        modelMapper.map(attendeeDto, attendee);
    }
}
