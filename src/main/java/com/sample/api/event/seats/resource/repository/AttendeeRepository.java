package com.sample.api.event.seats.resource.repository;

import com.sample.api.event.seats.resource.entity.AttendeeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface is used to interact with the db for the AttendeeEntity
 */
@Repository
public interface AttendeeRepository
        extends PagingAndSortingRepository<AttendeeEntity, Long> {
}
