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

import com.kla.lms.klamp.entity.Application;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.ApplicationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/application")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;

	@ApiOperation(value = "This method pulls the applications with Search Criteria")
	@GetMapping("/getApplicationsBySearchCriteria")
	public ResponseEntity<ResultPage<Application>> getApplicationsBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "applicationState", required = false, defaultValue = "") String applicationState,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "studentId", required = true, defaultValue = "0") long studentId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return applicationService.getApplicationBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy,  applicationState,  batchId,  studentId,
				 status);
	}

	@ApiOperation(value = "This method pulls All the applications")
	@GetMapping("getAllApplications")
	public ResponseEntity<List<Application>> getAllApplications() {
		return applicationService.getAllApplication();
	}

	@ApiOperation(value = "This method Creates New Applications")
	@PostMapping("/createApplications")
	public ResponseEntity<Application> createApplications(@RequestBody Application application) {
		return applicationService.createApplication(application);
	}

	@ApiOperation(value = "This method Updates the Applications")
	@PutMapping("/updateApplications") // Old application details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<Application> updateApplications(@RequestBody Application application) {
		return applicationService.updateApplication(application);
	}

	@ApiOperation(value = "This method returns the Reference id before new Applications Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return applicationService.getReferenceId();
	}
}