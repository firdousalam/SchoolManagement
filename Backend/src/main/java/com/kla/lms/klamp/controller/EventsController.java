package com.kla.lms.klamp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kla.lms.klamp.entity.Events;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.EventsService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {

	@Autowired
	EventsService eventsService;

	@ApiOperation(value = "This method pulls the events with Search Criteria")
	@GetMapping("/getEventsBySearchCriteria") // Search by event name, principal Name, status
	public ResponseEntity<ResultPage<Events>> getEventsBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "eventName", required = false, defaultValue = "") String eventName,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return eventsService.getEventsBySearchCriteria(pageNumber,pageSize,sortDirection,sortBy,eventName,status);
	}

	@ApiOperation(value = "This method pulls All the events")
	@GetMapping("getAllEvents")
	public ResponseEntity<List<Events>> getAllEvents() {
		return eventsService.getAllEvents();
	}

	@ApiOperation(value = "This method Creates New Event")
	@PostMapping("/createEvent")
	public ResponseEntity<Events> createEvent(@RequestBody Events event) {
		return eventsService.createEvents(event);
	}

	@ApiOperation(value = "This method Updates the Event")
	@PutMapping("/updateEvent") // Old event details will be persisted with new ones and the old one will
						// have status passive and date updated
	public ResponseEntity<Events> updateEvent(@RequestBody Events event) {
		return eventsService.updateEvents(event);
	}

	@ApiOperation(value = "This method returns the Reference id before new Events Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return eventsService.getReferenceId();
	}
}