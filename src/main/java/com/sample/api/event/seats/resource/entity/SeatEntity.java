package com.sample.api.event.seats.resource.entity;

import com.sample.api.event.seats.model.SeatTypeDto;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity object for a seat.
 */
@Entity
@Table(name = "seat")
public class SeatEntity implements Serializable {
    private static final long serialVersionUID = 528729541274912208L;
    private Long id;
    private Long eventId;
    private Boolean isAvailable;
    private Boolean isAisle;
    private SeatTypeDto seatTypeDto;

    @Id
    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "event_id")
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Column(name = "available")
    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    @Column(name = "aisle")
    public Boolean isAisle() {
        return isAisle;
    }

    public void setAisle(Boolean aisle) {
        isAisle = aisle;
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public SeatTypeDto getSeatTypeDto() {
        return seatTypeDto;
    }

    public void setSeatTypeDto(SeatTypeDto seatTypeDto) {
        this.seatTypeDto = seatTypeDto;
    }
}
