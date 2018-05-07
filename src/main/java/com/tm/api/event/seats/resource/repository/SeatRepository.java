package com.tm.api.event.seats.resource.repository;

import com.tm.api.event.seats.resource.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository
        extends PagingAndSortingRepository<SeatEntity, Long>, JpaSpecificationExecutor<SeatEntity> {
}
