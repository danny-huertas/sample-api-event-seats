package com.tm.api.event.seats.resource.dao;

import com.tm.api.event.seats.domain.Seat;
import com.tm.api.event.seats.resource.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeatDao {

    private SeatRepository seatRepository;

    @Autowired
    public SeatDao(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Get all Seat for a given Event that started after startTime and
     * finished before finishedTime.
     *
     * @return A page of Seat matching the criteria.
     */
    public Long getSeatCount(Seat seat) {
        return seatRepository.count(new SeatEntitySpecification(seat));
    }
}
