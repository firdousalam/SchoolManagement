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
import com.kla.lms.klamp.entity.Type;
import com.kla.lms.klamp.service.TypeService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/type")
public class TypeController {

	@Autowired
	TypeService typeService;

	@ApiOperation(value = "This method pulls the Types with Search Criteria")
	@GetMapping("/getTypeBySearchCriteria")
	public ResponseEntity<ResultPage<Type>> getTypeBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "referenceType", required = false, defaultValue = "") String referenceType,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return typeService.getTypeBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, referenceType, status);
	}

	@ApiOperation(value = "This method pulls All the configParams")
	@GetMapping("getAllType")
	public ResponseEntity<List<Type>> getAllType() {
		return typeService.getAllType();
	}

	@ApiOperation(value = "This method Creates New Type")
	@PostMapping("/createType")
	public ResponseEntity<Type> createType(@RequestBody Type type) {
		return typeService.createType(type);
	}

	@ApiOperation(value = "This method Updates the Type")
	@PutMapping("/updateType")
	public ResponseEntity<Type> updateType(@RequestBody Type type) {
		return typeService.updateType(type);
	}

	@ApiOperation(value = "This method returns the Reference id before new Type Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return typeService.getReferenceId();
	}
}