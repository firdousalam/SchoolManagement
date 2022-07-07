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

import com.kla.lms.klamp.entity.CourseCalendar;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.CourseCalendarService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/courseCalendar")
public class CourseCalendarController {

	@Autowired
	CourseCalendarService courseCalendarService;

	@ApiOperation(value = "This method pulls the courseCalendars with Search Criteria")
	@GetMapping("/getCourseCalendarBySearchCriteria")
	public ResponseEntity<ResultPage<CourseCalendar>> getCourseCalendarBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return courseCalendarService.getCourseCalendarBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy, batchId,  status);
	}

	@ApiOperation(value = "This method pulls All the courseCalendars")
	@GetMapping("getAllCourseCalendar")
	public ResponseEntity<List<CourseCalendar>> getAllCourseCalendar() {
		return courseCalendarService.getAllCourseCalendar();
	}

	@ApiOperation(value = "This method Creates New CourseCalendar")
	@PostMapping("/createCourseCalendar")
	public ResponseEntity<CourseCalendar> createCourseCalendar(@RequestBody CourseCalendar courseCalendar) {
		return courseCalendarService.createCourseCalendar(courseCalendar);
	}

	@ApiOperation(value = "This method Updates the CourseCalendar")
	@PutMapping("/updateCourseCalendar") // Old courseCalendar details will be persisted with new ones and the old one
											// will
	// have status passive and date updated
	public ResponseEntity<CourseCalendar> updateCourseCalendar(@RequestBody CourseCalendar courseCalendar) {
		return courseCalendarService.updateCourseCalendar(courseCalendar);
	}

	@ApiOperation(value = "This method returns the Reference id before new CourseCalendar Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return courseCalendarService.getReferenceId();
	}
}