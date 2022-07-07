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
import com.kla.lms.klamp.entity.SearchPage;
import com.kla.lms.klamp.entity.StudyMaterial;
import com.kla.lms.klamp.service.StudyMaterialService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/studyMaterial")
public class StudyMaterialController {

	@Autowired
	StudyMaterialService studyMaterialService;

	@ApiOperation(value = "This method pulls the studyMaterials with Search Criteria")
	@GetMapping("/getStudyMaterialsBySearchCriteria")
	public ResponseEntity<ResultPage<StudyMaterial>> getStudyMaterialsBySearchCriteria(SearchPage searchPage,
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "batchName", required = false, defaultValue = "") String batchName,
			@RequestParam(name = "heading", required = false, defaultValue = "") String heading,
			@RequestParam(name = "subject", required = false, defaultValue = "") String subject,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return studyMaterialService.getStudyMaterialBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy,
				 heading,  subject,  batchId, status);
	}

	@ApiOperation(value = "This method pulls All the studyMaterials")
	@GetMapping("getAllStudyMaterials")
	public ResponseEntity<List<StudyMaterial>> getAllStudyMaterials() {
		return studyMaterialService.getAllStudyMaterial();
	}

	@ApiOperation(value = "This method Creates New StudyMaterials")
	@PostMapping("/createStudyMaterials")
	public ResponseEntity<StudyMaterial> createStudyMaterials(@RequestBody StudyMaterial studyMaterial) {
		return studyMaterialService.createStudyMaterial(studyMaterial);
	}

	@ApiOperation(value = "This method Updates the StudyMaterials")
	@PutMapping("/updateStudyMaterials") // Old studyMaterial details will be persisted with new ones and the old one
											// will
	// have status passive and date updated
	public ResponseEntity<StudyMaterial> updateStudyMaterials(@RequestBody StudyMaterial studyMaterial) {
		return studyMaterialService.updateStudyMaterial(studyMaterial);
	}

	@ApiOperation(value = "This method returns the Reference id before new StudyMaterials Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return studyMaterialService.getReferenceId();
	}
}