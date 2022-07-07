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

import com.kla.lms.klamp.entity.Institution;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.InstitutionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/institutions")
public class InstitutionController {

	@Autowired
	InstitutionService institutionService;

	@ApiOperation(value = "This method pulls the institutions with Search Criteria")
	@GetMapping("/getInstitutionsBySearchCriteria")
	public ResponseEntity<ResultPage<Institution>> getInstitutionsBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "institutionName", required = false, defaultValue = "") String institutionName,
			@RequestParam(name = "principalName", required = false, defaultValue = "") String principalName,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return institutionService.getInstitutionBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy,
				institutionName, principalName, status);
	}

	@ApiOperation(value = "This method pulls All the institutions")
	@GetMapping("getAllInstitutions")
	public ResponseEntity<List<Institution>> getAllInstitutions() {
		return institutionService.getAllInstitutions();
	}

	@ApiOperation(value = "This method Creates New Institution")
	@PostMapping("/createInstitution")
	public ResponseEntity<Institution> createInstitution(@RequestBody Institution institution) {
		return institutionService.createInstitutions(institution);
	}

	@ApiOperation(value = "This method Updates the Institution")
	@PutMapping("/updateInstitution") // Old institution details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<Institution> updateInstitution(@RequestBody Institution institution) {
		return institutionService.updateInstitutions(institution);
	}

	@ApiOperation(value = "This method returns the Reference id before new Institution Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return institutionService.getReferenceId();
	}
}