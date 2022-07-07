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

import com.kla.lms.klamp.entity.BatchEvents;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.BatchEventService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/batchEvent")
public class BatchEventController {

	@Autowired
	BatchEventService batchEventService;

	@ApiOperation(value = "This method pulls the batch events with Search Criteria")
	@GetMapping("/getBatchEventBySearchCriteria")
	public ResponseEntity<ResultPage<BatchEvents>> getBatchEventBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "eventName", required = false, defaultValue = "") String eventName,
			@RequestParam(name = "parentEventId", required = false, defaultValue = "0") long parentEventId,
			@RequestParam(name = "batchId", required = false, defaultValue = "0") long batchId,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return batchEventService.getBatchEventBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, eventName,
				parentEventId, batchId, status);
	}

	@ApiOperation(value = "This method pulls All the batch Events")
	@GetMapping("getAllBatchEvents")
	public ResponseEntity<List<BatchEvents>> getAllBatchEvents() {
		return batchEventService.getAllBatchEvent();
	}

	@ApiOperation(value = "This method Creates New Batch Event")
	@PostMapping("/createBatchEvent")
	public ResponseEntity<BatchEvents> createBatchEvent(@RequestBody BatchEvents batchEvent) {
		return batchEventService.createBatchEvent(batchEvent);
	}
	
	@ApiOperation(value = "This method Creates New Batch Event")
	@PostMapping("/saveBatchEvents")
	public ResponseEntity<List<BatchEvents>> saveBatchEvents(@RequestParam Long batchId,
			@RequestBody List<Long> eventIdList) {
		return batchEventService.saveBatchEvents(batchId, eventIdList);
	}

	@ApiOperation(value = "This method Updates the Batch Event")
	@PutMapping("/updateBatchEvent") // Old batch events details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<BatchEvents> updateBatch(@RequestBody BatchEvents batchEvent) {
		return batchEventService.updateBatchEvent(batchEvent);
	}

	@ApiOperation(value = "This method returns the Reference id before new Batch Events Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return batchEventService.getReferenceId();
	}
}