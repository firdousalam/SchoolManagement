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

import com.kla.lms.klamp.entity.Data;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.DataService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {

	@Autowired
	DataService dataService;

	@ApiOperation(value = "This method pulls the datas with Search Criteria")
	@GetMapping("/getDataBySearchCriteria")
	public ResponseEntity<ResultPage<Data>> getDataBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "referenceType", required = false, defaultValue = "") String referenceType,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return dataService.getDataBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, name,referenceType, status);
	}

	@ApiOperation(value = "This method pulls All the data")
	@GetMapping("getAllData")
	public ResponseEntity<List<Data>> getAllData() {
		return dataService.getAllData();
	}

	@ApiOperation(value = "This method Creates New Data")
	@PostMapping("/createData")
	public ResponseEntity<Data> createData(@RequestBody Data data) {
		return dataService.createData(data);
	}

	@ApiOperation(value = "This method Updates the Data")
	@PutMapping("/updateData")
	public ResponseEntity<Data> updateData(@RequestBody Data data) {
		return dataService.updateData(data);
	}

	@ApiOperation(value = "This method returns the Reference id before new Data Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return dataService.getReferenceId();
	}
}