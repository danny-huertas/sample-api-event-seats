package com.sample.api.event.seats.controllers;

import com.sample.api.common.operation.Operation;
import com.sample.api.common.operation.Result;
import com.sample.api.event.seats.configuration.SpringRootConfig;
import com.sample.api.event.seats.controllers.model.AttendeeResponseDto;
import com.sample.api.event.seats.converters.AttendeeDtoToEntityConverter;
import com.sample.api.event.seats.converters.AttendeeEntityToDtoConverter;
import com.sample.api.event.seats.model.AttendeeDto;
import com.sample.api.event.seats.resource.entity.AttendeeEntity;
import com.sample.api.event.seats.service.AttendeeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for Attendees API. (The reference documentation is built with Swagger)
 */
@Controller
@RequestMapping(SpringRootConfig.ATTENDEE_REQUEST_MAPPING)
@Api(value = "attendees")
@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved count"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"), @ApiResponse(code = 406, message = "Not Acceptable"),
        @ApiResponse(code = 415, message = "Unsupported Media Type"),
        @ApiResponse(code = 500, message = "An unidentified exception has occurred.")})
public class AttendeeController {
    private Tracer tracer;
    private AttendeeService attendeeService;
    private AttendeeDtoToEntityConverter attendeeDtoToEntityConverter;
    private AttendeeEntityToDtoConverter attendeeEntityToDtoConverter;

    @Autowired
    public AttendeeController(Tracer tracer, AttendeeService attendeeService, AttendeeDtoToEntityConverter attendeeDtoToEntityConverter, AttendeeEntityToDtoConverter attendeeEntityToDtoConverter) {
        this.tracer = tracer;
        this.attendeeService = attendeeService;
        this.attendeeDtoToEntityConverter = attendeeDtoToEntityConverter;
        this.attendeeEntityToDtoConverter = attendeeEntityToDtoConverter;
    }

    /**
     * Get a list attendees
     *
     * @return AttendeeResponseDto AttendeeResponseDto object
     */
    @ApiOperation(value = "Get Attendees", notes = "Gets all the attendees", response = AttendeeResponseDto.class)
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttendeeResponseDto getAttendees() {
        //build a list of attendees
        List<AttendeeDto> attendeeDtos = new ArrayList<>();
        attendeeService.getAttendees().forEach(attendeeEntity -> {
            //attendee
            AttendeeDto attendee = new AttendeeDto();
            attendeeEntityToDtoConverter.convert(attendeeEntity, attendee);
            attendeeDtos.add(attendee);
        });


        //build and return the response dto object
        return new AttendeeResponseDto.AttendeeResponseDtoBuilder().setAttendeeDtos(attendeeDtos).setOperation(
                new Operation.ApiOperationBuilder().result(Result.OK)
                        .correlationId(tracer.getCurrentSpan().traceIdString())
                        .requestInstant(Instant.ofEpochMilli(tracer.getCurrentSpan().getBegin()).toString()).build())
                .build();
    }

    /**
     * Create an attendee
     *
     * @param attendee attendee dto
     * @return AttendeeResponseDto AttendeeResponseDto object
     */
    @ApiOperation(value = "Create Attendee", notes = "Creates an attendee", response = AttendeeResponseDto.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttendeeResponseDto createAttendee(@ApiParam(value = "request body", required = true) @Valid @RequestBody AttendeeDto attendee) {
        //create an attendee
        AttendeeEntity attendeeEntity = new AttendeeEntity();
        attendeeDtoToEntityConverter.convert(attendee, attendeeEntity);
        attendeeEntityToDtoConverter.convert(attendeeService.save(attendeeEntity), attendee);

        //build and return the response dto object
        return new AttendeeResponseDto.AttendeeResponseDtoBuilder().setAttendeeDto(attendee).setOperation(
                new Operation.ApiOperationBuilder().result(Result.OK)
                        .correlationId(tracer.getCurrentSpan().traceIdString())
                        .requestInstant(Instant.ofEpochMilli(tracer.getCurrentSpan().getBegin()).toString()).build())
                .build();
    }

    /**
     * Get an attendee
     *
     * @param attendeeId attendee id
     * @return AttendeeResponseDto AttendeeResponseDto object
     */
    @ApiOperation(value = "Get an Attendee", notes = "Gets an attendee", response = AttendeeResponseDto.class)
    @RequestMapping(value = "/{attendeeId}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttendeeResponseDto getAttendee(@ApiParam(value = "ID of the attendee you want to get", allowableValues = "1,2,3,4", required = true) @PathVariable Long attendeeId) {
        //get an attendee
        AttendeeDto attendee = new AttendeeDto();
        attendeeEntityToDtoConverter.convert(attendeeService.getAttendee(attendeeId), attendee);

        //build and return the response dto object
        return new AttendeeResponseDto.AttendeeResponseDtoBuilder().setAttendeeDto(attendee).setOperation(
                new Operation.ApiOperationBuilder().result(Result.OK)
                        .correlationId(tracer.getCurrentSpan().traceIdString())
                        .requestInstant(Instant.ofEpochMilli(tracer.getCurrentSpan().getBegin()).toString()).build())
                .build();
    }

    /**
     * Remove an attendee
     *
     * @param attendeeId attendee id
     * @return AttendeeResponseDto AttendeeResponseDto object
     */
    @ApiOperation(value = "Remove an Attendee", notes = "Removes an attendee", response = AttendeeResponseDto.class)
    @RequestMapping(value = "/{attendeeId}/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttendeeResponseDto removeAttendee(@ApiParam(value = "ID of the attendee you want to remove", allowableValues = "1,2,3,4", required = true) @PathVariable Long attendeeId) {
        //remove an attendee
        attendeeService.removeAttendee(attendeeId);

        //build and return the response dto object
        return new AttendeeResponseDto.AttendeeResponseDtoBuilder().setOperation(
                new Operation.ApiOperationBuilder().result(Result.OK)
                        .correlationId(tracer.getCurrentSpan().traceIdString())
                        .requestInstant(Instant.ofEpochMilli(tracer.getCurrentSpan().getBegin()).toString()).build())
                .build();
    }

    @ApiOperation(value = "Update Attendee", notes = "Updates an attendee", response = AttendeeResponseDto.class)
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateAttendees(@ApiParam(value = "request body", required = true) @Valid @RequestBody AttendeeDto attendee) {
        //TODO add PUT
    }
}