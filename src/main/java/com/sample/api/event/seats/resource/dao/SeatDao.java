package com.sample.api.event.seats.resource.dao;

import com.sample.api.event.seats.domain.Seat;
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
     * Retrieves seat count based on search criteria for a given event.
     *
     * @param seat domain object for a seat
     * @return Returns the count of seats that match the given search criteria within a given event.
     */
    public Long getSeatCount(Seat seat) {
        return seatRepository.count(new SeatEntitySpecification(seat));
    }
}
