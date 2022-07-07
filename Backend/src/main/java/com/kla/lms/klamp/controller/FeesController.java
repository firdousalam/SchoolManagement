package com.kla.lms.klamp.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kla.lms.klamp.entity.Fees;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.FeesService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/fees")
public class FeesController {
	public final static Logger logger = Logger.getLogger(FeesController.class.getName());

	@Autowired
	FeesService feeService;

	@ApiOperation(value = "This method pulls the Fees with Search Criteria")
	@GetMapping("/getFeesBySearchCriteria") // Search by Batch, category & Status
	public ResponseEntity<ResultPage<Fees>> getFeesBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "category", required = false, defaultValue = "") String category,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		logger.log(Level.INFO, "getFeesBySearchCriteria...");
		return feeService.getFeesBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy,   category, batchId,  status);
	}

	@ApiOperation(value = "This method pulls All the Fees")
	@GetMapping("/getAllfees")
	public ResponseEntity<List<Fees>> getAllfees() {
		return feeService.getAllfees();
	}

	@ApiOperation(value = "This method Creates New Fee")
	@PostMapping("/createFee")
	public ResponseEntity<Fees> createFee(@RequestBody Fees fee) {
		return feeService.createFee(fee);
	}

	@ApiOperation(value = "This method Updates the fee")
	@PutMapping("/updateFee") // Old fees will be retained and the new fees will be created with fresh
	// id and old fees will be updated with the end date
	public ResponseEntity<Fees> updateFee(@RequestBody Fees fee) {
		return feeService.updateFee(fee);
	}

	@ApiOperation(value = "This method returns the Fees Reference id before new Fees Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return feeService.getReferenceId();
	}
}