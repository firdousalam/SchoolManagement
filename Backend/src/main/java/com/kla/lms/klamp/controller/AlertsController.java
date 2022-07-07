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

import com.kla.lms.klamp.entity.Alerts;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.AlertService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertsController {

	@Autowired
	AlertService alertService;

	@ApiOperation(value = "This method pulls the alerts with Search Criteria")
	@GetMapping("/getAlertsBySearchCriteria")
	public ResponseEntity<ResultPage<Alerts>> getAlertsBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "eventName", required = false, defaultValue = "") String eventName,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return alertService.getAlertsBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, eventName,status);
	}
	
	@ApiOperation(value = "This method pulls All the alerts")
	@GetMapping("getAllAlerts")
	public ResponseEntity<List<Alerts>> getAllAlerts() {
		return alertService.getAllAlert();
	}

	@ApiOperation(value = "This method Creates New Alerts")
	@PostMapping("/createAlerts")
	public ResponseEntity<Alerts> createAlerts(@RequestBody Alerts alert) {
		return alertService.createAlert(alert);
	}

	@ApiOperation(value = "This method Updates the Alerts")
	@PutMapping("/updateAlerts") // Old alert details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<Alerts> updateAlerts(@RequestBody Alerts alert) {
		return alertService.updateAlert(alert);
	}

	@ApiOperation(value = "This method returns the Reference id before new Alerts Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return alertService.getReferenceId();
	}
}