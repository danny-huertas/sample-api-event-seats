package com.sample.api.event.seats.resource.repository;

import com.sample.api.event.seats.resource.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface is used to interact with the db for the SeatEntity
 */
@Repository
public interface SeatRepository
        extends PagingAndSortingRepository<SeatEntity, Long>, JpaSpecificationExecutor<SeatEntity> {
}
