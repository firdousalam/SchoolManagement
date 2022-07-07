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

import com.kla.lms.klamp.entity.Alerts;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.AlertRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class AlertService {
	public static final Logger logger = LoggerFactory.getLogger(AlertService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;

	@Autowired
	AlertRepo repository;

	public long getAlertCount() {
		logger.info("inside getAlertCount...");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Alerts>> getAlertsBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String eventName, Integer status) {
		logger.info("inside getAlertsBySearchCriteria..............");
		GenericSpecification<Alerts> genericSpecification = new GenericSpecification<>();
		GenericConversion<Alerts> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		if (StringUtils.hasText(eventName)) {
			genericSpecification.add(new SearchCriteria("eventName", eventName, SearchOperation.EQUAL));
		}
		
		if (status != null && status>-1) {
			genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Alerts> resultPage = null;
		if (pageSize > -1) {
			Page<Alerts> page = repository.findAll(genericSpecification, pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Alerts> alertsList = repository.findAll(genericSpecification, pageable.getSort());
			resultPage = genericConversion.convertToResultData(alertsList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Alerts>> getAllAlert() {
		logger.info("inside createAlert...");
		List<Alerts> alertsList = new ArrayList<>();
		try {
			alertsList = (List<Alerts>) repository.findAll();
			if (alertsList.isEmpty()) {
				logger.info("findAll(null): alerts");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(alertsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(alertsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Alerts> createAlert(Alerts alert) {
		logger.info("inside createAlert...");
		try {
			long id = getAlertCount();
			alert.setId(++id);
			alert.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			alert.setDateofEntry(Util.getDateOfEntry(timestamp));
			alert.setCreatedBy(Util.getDefaultUser());
			alert.setCreatedAt(timestamp);
			alert.setUpdatedBy(Util.getDefaultUser());
			alert.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(alert), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createAlert - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Alerts> updateAlert(Alerts alert) {
		logger.info("inside updateAlert...");
		try {
			
			Optional<Alerts> optionalAlerts = repository.findById(alert.getId());
			
			if (optionalAlerts.isPresent()) {
				Alerts existingAlerts = optionalAlerts.get();
				existingAlerts.setStatus(PASSIVE_STATUS);
				existingAlerts.setUpdatedBy(Util.getDefaultUser());
				existingAlerts.setUpdatedAt(Util.getTimeStamp());
				repository.save(existingAlerts);
			}
			alert.setId(0);
			alert.setUpdatedAt(null);
			alert.setUpdatedAt(null);
			return createAlert(alert);

		} catch (Exception e) {
			logger.error("update alerts for " + alert.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId...");
		return repository.count() + 1;
	}
}