package com.kla.lms.klamp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kla.lms.klamp.entity.Batch;
import com.kla.lms.klamp.entity.Notification;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.NotificationRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class NotificationService {
	public static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	NotificationRepo repository;

	/*
	 * Get count of notifications
	 */
	public long getNotificationCount() {
		logger.info("inside getNotificationCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Notification>> getNotificationBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String notificationType,long batchId, Integer status) {
		logger.info("inside getNotificationBySearchCriteria..............");
		GenericConversion<Notification> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<Batch,Notification> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batch");
		parentSpec.add(new SearchCriteria("batchId", batchId, SearchOperation.EQUAL));
		
		GenericSpecification<Notification> spec = new GenericSpecification<>();
		if (StringUtils.hasText(notificationType)) {
			spec.add(new SearchCriteria("notificationType", notificationType, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Notification> resultPage = null;
		if (pageSize > -1) {
			Page<Notification> page = repository.findAll(parentSpec.and(spec),pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Notification> notificationList = repository.findAll(parentSpec.and(spec),pageable.getSort());
			resultPage = genericConversion.convertToResultData(notificationList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Notification>> getAllNotification() {
		logger.info("inside getAllNotification..............");
		List<Notification> notificationsList = new ArrayList<>();
		try {
			notificationsList = (List<Notification>) repository.findAll();
			if (notificationsList.isEmpty()) {
				logger.info("findAll(null): notification");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(notificationsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(notificationsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Notification> createNotification(Notification notification) {
		logger.info("inside createNotification..............");
		try {
			long id = getNotificationCount();
			notification.setId(++id);
			notification.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			notification.setDateofEntry(Util.getDateOfEntry(timestamp));
			notification.setCreatedBy(Util.getDefaultUser());
			notification.setCreatedAt(timestamp);
			notification.setUpdatedBy(Util.getDefaultUser());
			notification.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(notification), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createNotification - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Notification> updateNotification(Notification notification) {
		logger.info("inside updateNotification..............");
		try {
			Optional<Notification> optionalNotification = repository.findById(notification.getId());
			if (!optionalNotification.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Notification existingNotification = optionalNotification.get();
			notification.setCreatedBy(existingNotification.getCreatedBy());
			notification.setCreatedAt(existingNotification.getCreatedAt());
			notification.setUpdatedBy(Util.getDefaultUser());
			notification.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(notification), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update notifications for " + notification.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
}