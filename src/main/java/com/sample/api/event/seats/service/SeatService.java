package com.sample.api.event.seats.service;

import com.sample.api.event.seats.model.SeatDto;
import com.sample.api.event.seats.resource.dao.SeatDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to interact with the dao for the SeatDto
 */
@Service
public class SeatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeatService.class);
    private SeatDao seatDao;

    @Autowired
    public SeatService(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    /**
     * Retrieves seatDto count based on search criteria for a given event.
     *
     * @param seatDto model object for a seatDto
     * @return Returns the count of seats that match the given search criteria within a given event.
     */
    public Long getSeatCount(SeatDto seatDto) {
        LOGGER.debug("retrieving seatDto count for seatDto={}  ", seatDto);
        return seatDao.getSeatCount(seatDto);
    }

}
