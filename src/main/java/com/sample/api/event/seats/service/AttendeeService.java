package com.sample.api.event.seats.service;

import com.sample.api.event.seats.resource.dao.AttendeeDao;
import com.sample.api.event.seats.resource.entity.AttendeeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to interact with the dao for the attendee
 */
@Service
public class AttendeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttendeeService.class);
    private AttendeeDao attendeeDao;

    @Autowired
    public AttendeeService(AttendeeDao attendeeDao) {
        this.attendeeDao = attendeeDao;
    }

    /**
     * Save an attendee
     *
     * @param attendee attendee entity
     * @return an attendee entity
     */
    public AttendeeEntity save(AttendeeEntity attendee) {
        return attendeeDao.save(attendee);
    }

    /**
     * Get a all attendees
     *
     * @return a collection of attendees
     */
    public Iterable<AttendeeEntity> getAttendees() {
        return attendeeDao.getAttendees();
    }

    /**
     * Get an attendee
     *
     * @param id attendee id
     * @return an attendee entity
     */
    public AttendeeEntity getAttendee(Long id) {
        LOGGER.debug("retrieving attendee for id={}  ", id);
        return attendeeDao.getAttendee(id);
    }

    /**
     * Remove an attendee
     *
     * @param id attendee id
     */
    public void removeAttendee(Long id) {
        attendeeDao.removeAttendee(id);
    }
}
