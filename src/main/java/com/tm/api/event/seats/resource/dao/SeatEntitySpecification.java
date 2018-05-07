package com.tm.api.event.seats.resource.dao;

import com.tm.api.common.exception.InvalidQueryParamException;
import com.tm.api.event.seats.domain.Seat;
import com.tm.api.event.seats.resource.entity.SeatEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeatEntitySpecification implements Specification<SeatEntity> {
    private Seat seat;

    SeatEntitySpecification(Seat seat) {
        this.seat = seat;
    }

    @Override
    public Predicate toPredicate(Root<SeatEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.and(addPredicates(root, cb));
    }

    private Predicate[] addPredicates(Root<SeatEntity> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Arrays.stream(seat.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                Object value = field.get(seat);
                if (value != null) {
                    predicates.add(cb.equal(root.get(field.getName()), value));
                }
            } catch (IllegalAccessException e) {
                throw new InvalidQueryParamException(field.getName());
            }
        });

        return predicates.toArray(new Predicate[0]);
    }
}
