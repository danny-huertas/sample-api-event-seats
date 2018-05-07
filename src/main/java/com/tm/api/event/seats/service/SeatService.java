package com.tm.api.event.seats.service;

import com.tm.api.event.seats.domain.Seat;
import com.tm.api.event.seats.resource.dao.SeatDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeatService.class);

    private SeatDao seatDao;

    @Autowired
    public SeatService(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    /**
     * Get a collection of Event seat for a given Event Id.
     *
     * @return A page of Event seat associated with a Event.
     */
    public Long getSeatCount(final Seat seat) {
        LOGGER.debug("retrieving seat count for seat={}  ", seat);
        return seatDao.getSeatCount(seat);
    }

}
