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
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.service.StudentProfileService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studentProfile")
public class StudentProfileController {

	@Autowired
	StudentProfileService studentProfileService;

	@ApiOperation(value = "This method pulls the StudentProfiles  with Search Criteria")
	@GetMapping("/getStudentProfileBySearchCriteria")
	public ResponseEntity<ResultPage<StudentProfile>> getStudentProfileBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "userId", required = false, defaultValue = "") String userId,
			@RequestParam(name = "profileId", required = false, defaultValue = "0") long profileId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studentProfileService.getStudentProfileBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy,  userId, profileId, status);
	}
	
	@ApiOperation(value = "This method pulls All the student profile")
	@GetMapping("/getAllStudentProfile")
	public ResponseEntity<List<StudentProfile>> getAllStudentProfile() {
		return studentProfileService.getAllStudentProfile();
	}

	@ApiOperation(value = "This method Creates New StudentProfile")
	@PostMapping("/createStudentProfile")
	public ResponseEntity<StudentProfile> createStudentProfile(
			@RequestBody StudentProfile studentProfile) {
		return studentProfileService.createStudentProfile(studentProfile);
	}

	@ApiOperation(value = "This method Updates the StudentProfile")
	@PutMapping("/updateStudentProfile") // Old student profile details will be persisted with new ones and
												// the old one will
	// have status passive and date updated
	public ResponseEntity<StudentProfile> updateStudentProfile(
			@RequestBody StudentProfile studentProfile) {
		return studentProfileService.updateStudentProfile(studentProfile);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudentProfile Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studentProfileService.getReferenceId();
	}
}