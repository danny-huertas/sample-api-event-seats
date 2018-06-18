package com.sample.api.event.seats.resource.dao;

import com.sample.api.event.seats.resource.entity.AttendeeEntity;
import com.sample.api.event.seats.resource.repository.AttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to interact with the db for the AttendeeEntity
 */
@Component
public class AttendeeDao {
    private AttendeeRepository seatRepository;

    @Autowired
    public AttendeeDao(AttendeeRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Save an attendee
     *
     * @param attendee attendee entity
     * @return an attendee entity
     */
    public AttendeeEntity save(AttendeeEntity attendee) {
        return seatRepository.save(attendee);
    }

    /**
     * Get a all attendees
     *
     * @return a collection of attendees
     */
    public Iterable<AttendeeEntity> getAttendees() {
        return seatRepository.findAll();
    }

    /**
     * Get an attendee
     *
     * @param id attendee id
     * @return an attendee entity
     */
    public AttendeeEntity getAttendee(Long id) {
        return seatRepository.findOne(id);
    }

    /**
     * Remove an attendee
     *
     * @param id attendee id
     */
    public void removeAttendee(Long id) {
        seatRepository.delete(id);
    }
}
