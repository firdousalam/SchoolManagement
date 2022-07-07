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

import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.StudentEducations;
import com.kla.lms.klamp.service.StudentEducationDetailService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studentEducation")
public class StudentEducationController {

	@Autowired
	StudentEducationDetailService studentEducationService;

	@ApiOperation(value = "This method pulls the Student Education with Search Criteria")
	@GetMapping("/getStudentEducationBySearchCriteria")
	public ResponseEntity<ResultPage<StudentEducations>> getStudentEducationBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "profileId", required = false, defaultValue = "") long profileId,
			@RequestParam(name = "courseName", required = false, defaultValue = "") String courseName,
			@RequestParam(name = "institution", required = false, defaultValue = "") String institution,
			@RequestParam(name = "educationStatus", required = false, defaultValue = "") String educationStatus,
			@RequestParam(name = "yearofCompletion", required = false, defaultValue = "") String yearofCompletion,
			@RequestParam(name = "percentage", required = false, defaultValue = "") String percentage,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studentEducationService.getStudentEducationBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy,
				profileId, courseName, institution, educationStatus, yearofCompletion, percentage, status);
	}

	@ApiOperation(value = "This method pulls All the studentEducations")
	@GetMapping("getAllStudentEducations")
	public ResponseEntity<List<StudentEducations>> getAllStudentEducations() {
		return studentEducationService.getAllStudentEducation();
	}

	@ApiOperation(value = "This method Creates New StudentEducations")
	@PostMapping("/createStudentEducations")
	public ResponseEntity<StudentEducations> createStudentEducations(@RequestBody StudentEducations studentEducation,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentEducationService.createStudentEducation(studentEducation, profileId);
	}

	@ApiOperation(value = "This method Updates the StudentEducations")
	@PutMapping("/updateStudentEducations") // Old studentEducation details will be persisted with new ones and the old
											// one will
	// have status passive and date updated
	public ResponseEntity<StudentEducations> updateStudentEducations(@RequestBody StudentEducations studentEducation,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentEducationService.updateStudentEducation(studentEducation, profileId);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudentEducations Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studentEducationService.getReferenceId();
	}
}