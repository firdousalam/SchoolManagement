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
import com.kla.lms.klamp.entity.StudentContactDetails;
import com.kla.lms.klamp.service.StudentContactDetailService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studentContactDeatils")
public class StudentContactDetailController {

	@Autowired
	StudentContactDetailService studentContactDetailService;

	@ApiOperation(value = "This method pulls the Student Contact Details with Search Criteria")
	@GetMapping("/getStudentContactDetailBySearchCriteria")
	public ResponseEntity<ResultPage<StudentContactDetails>> getStudentContactDetailBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId,
			@RequestParam(name = "mobileNumber", required = false, defaultValue = "") String mobileNumber,
			@RequestParam(name = "emailId", required = false, defaultValue = "") String emailId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studentContactDetailService.getStudentContactDetailBySearchCriteria(pageNumber, pageSize, sortDirection,
				sortBy, profileId, mobileNumber, emailId, status);
	}

	@ApiOperation(value = "This method pulls All the studentContactDetails")
	@GetMapping("getAllStudentContactDetails")
	public ResponseEntity<List<StudentContactDetails>> getAllStudentContactDetails() {
		return studentContactDetailService.getAllStudentContactDetail();
	}

	@ApiOperation(value = "This method Creates New StudentContactDetails")
	@PostMapping("/createStudentContactDetails")
	public ResponseEntity<StudentContactDetails> createStudentContactDetails(
			@RequestBody StudentContactDetails studentContactDetail,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentContactDetailService.createStudentContactDetail(studentContactDetail, profileId);
	}

	@ApiOperation(value = "This method Updates the StudentContactDetails")
	@PutMapping("/updateStudentContactDetails") // Old studentContactDetail details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<StudentContactDetails> updateStudentContactDetails(@RequestBody StudentContactDetails studentContactDetail,@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId ){
		return studentContactDetailService.updateStudentContactDetail(studentContactDetail,profileId);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudentContactDetails Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studentContactDetailService.getReferenceId();
	}
}