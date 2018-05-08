package com.tm.api.event.seats.controllers;

import com.tm.api.common.operation.Operation;
import com.tm.api.common.operation.Result;
import com.tm.api.event.seats.configuration.SpringRootConfig;
import com.tm.api.event.seats.controllers.model.SeatResponseDto;
import com.tm.api.event.seats.domain.Seat;
import com.tm.api.event.seats.service.SeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

/**
 * REST controller for Event Seats API. (The reference documentation is built with Swagger)
 */
@Controller
@RequestMapping(SpringRootConfig.REQUEST_MAPPING)
@Api(value = "seats")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved count"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"), @ApiResponse(code = 406, message = "Not Acceptable"),
        @ApiResponse(code = 415, message = "Unsupported Media Type"),
        @ApiResponse(code = 500, message = "An unidentified exception has occurred.") })
public class SeatController {
    private Tracer tracer;
    private SeatService eventService;

    @Autowired
    public SeatController(Tracer tracer, SeatService seatService) {
        this.tracer = tracer;
        this.eventService = seatService;
    }

    /**
     * Retrieves seat count based on search criteria for a given event.
     * If no search criteria is provided, a count of all event seats is returned.
     *
     * @param eventId the event id being used for the search of seats
     * @param isAvailable filter by available seats. This is an optional parameter.
     * @param isAisle filter by aisle seats. This is an optional parameter.
     * @param type filter by the seat type (adult|child). This is an optional parameter.
     * @return Returns the count of seats that match the given search criteria within a given event.
     */
    @ApiOperation(value = "Event seat count", notes = "Get an event seat count", response = SeatResponseDto.class)
    @RequestMapping(value = "/{eventId}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SeatResponseDto getSeatCount(
            @ApiParam(value = "ID of the event you want the seat count for", allowableValues = "1,2,3,4", required = true) @PathVariable Long eventId,
            @ApiParam(value = "Filter count on seat availability") @RequestParam(name = "available", required = false) Boolean isAvailable,
            @ApiParam(value = "Filter count on aisle seats") @RequestParam(name = "aisle", required = false) Boolean isAisle,
            @ApiParam(value = "Filter count by type", allowableValues = "adult,child") @RequestParam(name = "type", required = false) String type) {

        //build a seat object and use it to get the count of seats for an event
        Long seatCount = eventService.getSeatCount(
                new Seat.SeatBuilder().eventId(eventId).isAvailable(isAvailable).isAisle(isAisle).seatType(type)
                        .build());

        //build and return the response dto object
        return new SeatResponseDto.SeatResponseDtoBuilder().setSeatCount(seatCount).setOperation(
                new Operation.ApiOperationBuilder().result(Result.OK)
                        .correlationId(tracer.getCurrentSpan().traceIdString())
                        .requestInstant(Instant.ofEpochMilli(tracer.getCurrentSpan().getBegin()).toString()).build())
                .build();
    }
}