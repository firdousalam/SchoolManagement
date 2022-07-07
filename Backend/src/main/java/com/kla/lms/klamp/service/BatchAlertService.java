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

import com.kla.lms.klamp.entity.BatchAlerts;
import com.kla.lms.klamp.entity.BatchEvents;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.BatchAlertRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class BatchAlertService {
	public static final Logger logger = LoggerFactory.getLogger(BatchAlertService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	BatchAlertRepo repository;
	
	@Autowired
	BatchEventService batchEventService;

	public long getBatchAlertCount() {
		logger.info("inside getBatchAlertCount...");
		return repository.count();
	}

	public ResponseEntity<ResultPage<BatchAlerts>> getBatchAlertsBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, long batchId, long batchEventId, long parentEventId, Integer status) {
		logger.info("inside getBatchBySearchCriteria..............");
		GenericConversion<BatchAlerts> genericConversion = new GenericConversion<>();
		
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<BatchEvents,BatchAlerts> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batchEvents");
		
		if (batchEventId > 0) {
			parentSpec.add(new SearchCriteria("id", batchEventId, SearchOperation.EQUAL));
		} else {
			parentSpec.add(new SearchCriteria("id", 0, SearchOperation.GREATER_THAN));
		}
		
		if (parentEventId > 0) {
			parentSpec.add(new SearchCriteria("parentEventId", parentEventId, SearchOperation.EQUAL));
		} else {
			parentSpec.add(new SearchCriteria("parentEventId", 0, SearchOperation.GREATER_THAN));
		}

		GenericSpecification<BatchAlerts> spec = new GenericSpecification<>();
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		if (batchId > 0) {
			spec.add(new SearchCriteria("batchId", batchId, SearchOperation.EQUAL));
		}
		
		ResultPage<BatchAlerts> resultPage = null;
		if (pageSize > -1) {
			Page<BatchAlerts> page = repository.findAll(parentSpec.and(spec),pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<BatchAlerts> batchAlertsList = repository.findAll(parentSpec.and(spec),pageable.getSort());
			resultPage = genericConversion.convertToResultData(batchAlertsList);
		}
		
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<BatchAlerts>> getAllBatchAlerts() {
		logger.info("inside getAllBatch...");
		List<BatchAlerts> batchAlertList = new ArrayList<>();
		try {
			batchAlertList = (List<BatchAlerts>) repository.findAll();
			if (batchAlertList.isEmpty()) {
				logger.info("findAll(null): batchAlerts");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(batchAlertList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(batchAlertList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<BatchAlerts> createBatchAlert(BatchAlerts batchAlerts) {
		logger.info("inside createBatch...");
		try {
			BatchEvents batchEvents = getBatchEvent(batchAlerts.getBatchEventId());
			long batchId = batchEvents.getBatchId();
			batchAlerts.setBatchEvents(batchEvents);
			batchAlerts.setBatchId(batchId);
			batchAlerts.setEventName(batchEvents.getEventName());
			
			long id = getBatchAlertCount();
			batchAlerts.setId(++id);
			batchAlerts.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			batchAlerts.setDateofEntry(Util.getDateOfEntry(timestamp));
			batchAlerts.setCreatedBy(Util.getDefaultUser());
			batchAlerts.setCreatedAt(timestamp);
			batchAlerts.setUpdatedBy(Util.getDefaultUser());
			batchAlerts.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(batchAlerts), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createBatch - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<BatchAlerts> updateBatchAlerts(BatchAlerts batchAlerts) {
		logger.info("inside updateBatchAlerts...");
		try {
			Optional<BatchAlerts> optionalBatchAlerts = repository.findById(batchAlerts.getId());
			if (!optionalBatchAlerts.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			BatchAlerts existingBatchAlerts = optionalBatchAlerts.get();
			batchAlerts.setCreatedBy(existingBatchAlerts.getCreatedBy());
			batchAlerts.setCreatedAt(existingBatchAlerts.getCreatedAt());
			batchAlerts.setUpdatedBy(Util.getDefaultUser());
			batchAlerts.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(batchAlerts), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update batch for " + batchAlerts.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId...");
		return repository.count() + 1;
	}
	
	private BatchEvents getBatchEvent(long batchEventId) {
		return batchEventService.getById(batchEventId);
	}
}