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

import com.kla.lms.klamp.entity.Batch;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.BatchService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/batch")
public class BatchController {

	@Autowired
	BatchService batchService;

	@ApiOperation(value = "This method pulls the batchs with Search Criteria")
	@GetMapping("/getBatchBySearchCriteria")
	public ResponseEntity<ResultPage<Batch>> getBatchBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "batchName", required = false, defaultValue = "") String batchName,
			@RequestParam(name = "batchNumber", required = false, defaultValue = "") String batchNumber,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return batchService.getBatchBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, batchName,
				batchNumber, status);
	}

	@ApiOperation(value = "This method pulls All the batchs")
	@GetMapping("getAllBatch")
	public ResponseEntity<List<Batch>> getAllBatch() {
		return batchService.getAllBatch();
	}

	@ApiOperation(value = "This method Creates New Batch")
	@PostMapping("/createBatch")
	public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
		return batchService.createBatch(batch);
	}

	@ApiOperation(value = "This method Updates the Batch")
	@PutMapping("/updateBatch") // Old batch details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<Batch> updateBatch(@RequestBody Batch batch) {
		return batchService.updateBatch(batch);
	}

	@ApiOperation(value = "This method returns the Reference id before new Batch Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return batchService.getReferenceId();
	}
}