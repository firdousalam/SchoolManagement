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
import com.kla.lms.klamp.entity.StudentProfessionDetails;
import com.kla.lms.klamp.service.StudentProfessionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studentProfession")
public class StudentProfessionController {

	@Autowired
	StudentProfessionService studentProfessionService;

	@ApiOperation(value = "This method pulls the Types with Search Criteria")
	@GetMapping("/getStudentProfessionBySearchCriteria")
	public ResponseEntity<ResultPage<StudentProfessionDetails>> getTypeBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId,
			@RequestParam(name = "organization", required = false, defaultValue = "") String organization,
			@RequestParam(name = "designation", required = false, defaultValue = "") String designation,
			@RequestParam(name = "placeOfDuty", required = false, defaultValue = "") String placeOfDuty,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studentProfessionService.getStudentProfessionBySearchCriteria(  pageNumber,  pageSize,
				 sortDirection,  sortBy, profileId,
				 organization,  designation,  placeOfDuty,  status);
	}


	@ApiOperation(value = "This method pulls All the studentProfessions")
	@GetMapping("getAllStudentProfessions")
	public ResponseEntity<List<StudentProfessionDetails>> getAllStudentProfessions() {
		return studentProfessionService.getAllStudentProfession();
	}

	@ApiOperation(value = "This method Creates New StudentProfessions")
	@PostMapping("/createStudentProfessions")
	public ResponseEntity<StudentProfessionDetails> createStudentProfessions(
			@RequestBody StudentProfessionDetails studentProfession,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentProfessionService.createStudentProfession(studentProfession,profileId);
	}

	@ApiOperation(value = "This method Updates the StudentProfessions")
	@PutMapping("/updateStudentProfessions") // Old studentProfession details will be persisted with new ones and the
												// old one will
	// have status passive and date updated
	public ResponseEntity<StudentProfessionDetails> updateStudentProfessions(
			@RequestBody StudentProfessionDetails studentProfession,@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentProfessionService.updateStudentProfession(studentProfession,profileId);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudentProfessions Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studentProfessionService.getReferenceId();
	}
}