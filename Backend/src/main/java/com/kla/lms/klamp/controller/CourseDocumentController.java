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

import com.kla.lms.klamp.entity.CourseDocument;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.CourseDocumentService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/courseDocument")
public class CourseDocumentController {

	@Autowired
	CourseDocumentService cdService;

	@ApiOperation(value = "This method pulls the Course Document with Search Criteria")
	@GetMapping("/getCourseDocumentBySearchCriteria")
	public ResponseEntity<ResultPage<CourseDocument>> getCourseDocumentBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "documentType", required = false, defaultValue = "") String documentType,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return cdService.getCourseDocumentBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy,  documentType,  batchId, status);
	}

	@ApiOperation(value = "This method pulls All the Course Document")
	@GetMapping("getAllCourseDocument")
	public ResponseEntity<List<CourseDocument>> getAllCourseDocument() {
		return cdService.getAllCourseDocument();
	}

	@ApiOperation(value = "This method Creates New Course Document")
	@PostMapping("/createCourseDocument")
	public ResponseEntity<CourseDocument> createAlerts(@RequestBody CourseDocument courseDocument) {
		return cdService.createCourseDocument(courseDocument);
	}

	@ApiOperation(value = "This method Updates the Course Document")
	@PutMapping("/updateCourseDocument") // Old documemts details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<CourseDocument> updateAlerts(@RequestBody CourseDocument courseDocument) {
		return cdService.updateCourseDocument(courseDocument);
	}

	@ApiOperation(value = "This method returns the Reference id before new Course Documents Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return cdService.getReferenceId();
	}
}