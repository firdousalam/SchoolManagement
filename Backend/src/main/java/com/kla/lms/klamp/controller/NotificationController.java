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

import com.kla.lms.klamp.entity.Notification;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.NotificationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

	@Autowired
	NotificationService notificationService;

	@ApiOperation(value = "This method pulls the notifications with Search Criteria")
	@GetMapping("/getNotificationsBySearchCriteria")
	public ResponseEntity<ResultPage<Notification>> getNotificationsBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "batchId", required = true, defaultValue = "0") long batchId,
			@RequestParam(name = "notificationType", required = false, defaultValue = "") String notificationType,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return notificationService.getNotificationBySearchCriteria( pageNumber,  pageSize,
				 sortDirection,  sortBy,  notificationType, batchId,  status);
	}

	@ApiOperation(value = "This method pulls All the notifications")
	@GetMapping("getAllNotifications")
	public ResponseEntity<List<Notification>> getAllNotifications() {
		return notificationService.getAllNotification();
	}

	@ApiOperation(value = "This method Creates New Notifications")
	@PostMapping("/createNotifications")
	public ResponseEntity<Notification> createNotifications(@RequestBody Notification notification) {
		return notificationService.createNotification(notification);
	}

	@ApiOperation(value = "This method Updates the Notifications")
	@PutMapping("/updateNotifications") // Old notification details will be persisted with new ones and the old one will
	// have status passive and date updated
	public ResponseEntity<Notification> updateNotifications(@RequestBody Notification notification) {
		return notificationService.updateNotification(notification);
	}

	@ApiOperation(value = "This method returns the Reference id before new Notifications Creation")
	@GetMapping("/getReferenceId")
	public long getReferenceId() {
		return notificationService.getReferenceId();
	}
}