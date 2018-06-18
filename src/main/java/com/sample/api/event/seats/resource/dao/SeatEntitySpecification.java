package com.sample.api.event.seats.resource.dao;

import com.sample.api.common.exception.InvalidQueryParamException;
import com.sample.api.event.seats.model.SeatDto;
import com.sample.api.event.seats.resource.entity.SeatEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to for specification in the sense of Domain Driven Design on a SeatEntity.
 */
public class SeatEntitySpecification implements Specification<SeatEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeatEntitySpecification.class);
    private SeatDto seatDto;

    SeatEntitySpecification(SeatDto seatDto) {
        this.seatDto = seatDto;
    }

    /**
     * Creates a WHERE clause for a query of the seatDto entity in form of a {@link Predicate} for the given
     * {@link Root} and {@link CriteriaQuery}.
     *
     * @param root  seatDto entity root
     * @param query criteria query for the seatDto entity
     * @return a {@link Predicate} built off the criteria builder for a seatDto entity
     */
    @Override
    public Predicate toPredicate(Root<SeatEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.and(addPredicates(root, cb));
    }

    /**
     * Adds predicates for the given {@link Root} and {@link CriteriaQuery}.
     *
     * @param root seatDto entity root
     * @param cb   criteria builder for the seatDto entity
     * @return a {@link Predicate} array built off the criteria builder for a seatDto entity
     */
    private Predicate[] addPredicates(Root<SeatEntity> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        //add any seatDto properties that are not null to the list of predicates
        Arrays.stream(seatDto.getClass().getDeclaredFields()).filter(field -> !field.isSynthetic()).forEach(field -> {
            try {
                field.setAccessible(true);
                Object value = field.get(seatDto);

                //if the field isn't null, add it to the list of predicates
                if (value != null) {
                    predicates.add(cb.equal(root.get(field.getName()), value));
                }
            } catch (IllegalAccessException ex) {
                LOGGER.error("Invalid Query Param: ", ex);
                throw new InvalidQueryParamException(field.getName());
            }
        });

        return predicates.toArray(new Predicate[0]);
    }
}
