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

import com.kla.lms.klamp.entity.BatchAlerts;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.BatchAlertService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/batchAlerts")
public class BatchAlertController {

	@Autowired
	BatchAlertService batchAlertsService;

	@ApiOperation(value = "This method pulls the batch Alertss with Search Criteria")
	@GetMapping("/getBatchAlertsBySearchCriteria")
	public ResponseEntity<ResultPage<BatchAlerts>> getBatchBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "batchEventId", required = false, defaultValue = "0") long batchEventId,
			@RequestParam(name = "parentEventId", required = false, defaultValue = "0") long parentEventId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return batchAlertsService.getBatchAlertsBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, batchId, batchEventId, parentEventId, status);
	}

	@ApiOperation(value = "This method pulls All the batchAlerts")
	@GetMapping("getAllBatchAlerts")
	public ResponseEntity<List<BatchAlerts>> getAllBatchAlerts() {
		return batchAlertsService.getAllBatchAlerts();
	}

	@ApiOperation(value = "This method Creates New Batch Alert")
	@PostMapping("/createBatchAlert")
	public ResponseEntity<BatchAlerts> createBatchAlert(@RequestBody BatchAlerts batchAlerts) {
		return batchAlertsService.createBatchAlert(batchAlerts);
	}

	@ApiOperation(value = "This method Updates the Batch Alert")
	@PutMapping("/updateBatchAlert") // Old batchAlerts details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<BatchAlerts> updateBatchAlerts(@RequestBody BatchAlerts batchAlerts) {
		return batchAlertsService.updateBatchAlerts(batchAlerts);
	}

	@ApiOperation(value = "This method returns the Reference id before new Batch Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return batchAlertsService.getReferenceId();
	}
}