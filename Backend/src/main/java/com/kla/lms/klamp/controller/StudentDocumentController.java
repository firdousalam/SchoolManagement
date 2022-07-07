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
import com.kla.lms.klamp.entity.StudentDocuments;
import com.kla.lms.klamp.service.StudentDocumentService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studentDocument")
public class StudentDocumentController {

	@Autowired
	StudentDocumentService studentDocumentService;

	@ApiOperation(value = "This method pulls the Student Document with Search Criteria")
	@GetMapping("/getStudentDocumentBySearchCriteria")
	public ResponseEntity<ResultPage<StudentDocuments>> getStudentDocumentBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "profileId", required = false, defaultValue = "") long profileId,
			@RequestParam(name = "documentType", required = false, defaultValue = "") String documentType,
			@RequestParam(name = "documentName", required = false, defaultValue = "") String documentName,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studentDocumentService.getStudentDocumentBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy,
				profileId, documentType, documentName, status);
	}

	@ApiOperation(value = "This method pulls All the studentDocuments")
	@GetMapping("getAllStudentDocuments")
	public ResponseEntity<List<StudentDocuments>> getAllStudentDocuments() {
		return studentDocumentService.getAllStudentDocument();
	}

	@ApiOperation(value = "This method Creates New StudentDocuments")
	@PostMapping("/createStudentDocuments")
	public ResponseEntity<StudentDocuments> createStudentDocuments(@RequestBody StudentDocuments studentDocument,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentDocumentService.createStudentDocument(studentDocument, profileId);
	}

	@ApiOperation(value = "This method Updates the StudentDocuments")
	@PutMapping("/updateStudentDocuments") // Old studentDocument details will be persisted with new ones and the old
											// one will
	// have status passive and date updated
	public ResponseEntity<StudentDocuments> updateStudentDocuments(@RequestBody StudentDocuments studentDocument,
			@RequestParam(name = "profileId", required = true, defaultValue = "") long profileId) {
		return studentDocumentService.updateStudentDocument(studentDocument, profileId);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudentDocuments Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studentDocumentService.getReferenceId();
	}
}