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
import com.kla.lms.klamp.entity.StudentPersonalDetails;
import com.kla.lms.klamp.service.StudentPersonalDetailService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studentpersonalDetails")
public class StudentPersonalDetailController {

	@Autowired
	StudentPersonalDetailService studentPersonalDetailService;

	@ApiOperation(value = "This method pulls the Student personal Details with Search Criteria")
	@GetMapping("/getStudentPersonalDetailBySearchCriteria")
	public ResponseEntity<ResultPage<StudentPersonalDetails>> getStudentPersonalDetailBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId,
			@RequestParam(name = "gender", required = false, defaultValue = "") String gender,
			@RequestParam(name = "nationality", required = false, defaultValue = "") String nationality,
			@RequestParam(name = "scOrSt", required = false, defaultValue = "") String scOrSt,
			@RequestParam(name = "isEmployed", required = false, defaultValue = "") String isEmployed,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studentPersonalDetailService.getStudentPersonalDetailBySearchCriteria(  pageNumber,  pageSize,
				 sortDirection,  sortBy, profileId,
				  gender,  nationality,  scOrSt,  isEmployed,  status);
	}

	@ApiOperation(value = "This method pulls All the studentPersonalDetails")
	@GetMapping("getAllStudentPersonalDetail")
	public ResponseEntity<List<StudentPersonalDetails>> getAllStudentPersonalDetail() {
		return studentPersonalDetailService.getAllStudentPersonalDetail();
	}

	@ApiOperation(value = "This method Creates New StudentPersonalDetail")
	@PostMapping("/createStudentPersonalDetail")
	public ResponseEntity<StudentPersonalDetails> createStudentPersonalDetail(
			@RequestBody StudentPersonalDetails studentPersonalDetail,@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentPersonalDetailService.createStudentPersonalDetail(studentPersonalDetail,profileId);
	}

	@ApiOperation(value = "This method Updates the StudentPersonalDetail")
	@PutMapping("/updateStudentPersonalDetail") // Old studentPersonalDetail details will be persisted with new ones and
												// the old one will
	// have status passive and date updated
	public ResponseEntity<StudentPersonalDetails> updateStudentPersonalDetail(
			@RequestBody StudentPersonalDetails studentPersonalDetail,@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentPersonalDetailService.updateStudentPersonalDetail(studentPersonalDetail,profileId);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudentPersonalDetail Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studentPersonalDetailService.getReferenceId();
	}
}