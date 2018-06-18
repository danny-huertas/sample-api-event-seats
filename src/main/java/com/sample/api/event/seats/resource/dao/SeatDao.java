package com.sample.api.event.seats.resource.dao;

import com.sample.api.event.seats.model.SeatDto;
import com.sample.api.event.seats.resource.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to interact with the db for the SeatEntity
 */
@Component
public class SeatDao {
    private SeatRepository seatRepository;

    @Autowired
    public SeatDao(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Retrieves seatDto count based on search criteria for a given event.
     *
     * @param seatDto model object for a seatDto
     * @return Returns the count of seats that match the given search criteria within a given event.
     */
    public Long getSeatCount(SeatDto seatDto) {
        return seatRepository.count(new SeatEntitySpecification(seatDto));
    }
}
