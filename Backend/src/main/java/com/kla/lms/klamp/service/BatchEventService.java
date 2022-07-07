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
import com.kla.lms.klamp.entity.BatchEvents;
import com.kla.lms.klamp.entity.Events;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.exception.AppException;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.BatchEventRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class BatchEventService {
	public static final Logger logger = LoggerFactory.getLogger(BatchEventService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	BatchEventRepo repository;
	
	@Autowired
	BatchService batchService;
	
	@Autowired
	EventsService eventsService;

	public long getBatchEventCount() {
		logger.info("inside getBatchEventCount...");
		return repository.count();
	}

	public ResponseEntity<ResultPage<BatchEvents>> getBatchEventBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String eventName, long parentEventId, long batchId, Integer status) {
		logger.info("inside getBatchEventBySearchCriteria..............");
		GenericConversion<BatchEvents> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<Batch, BatchEvents> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batch");
		if (batchId > 0) {
			parentSpec.add(new SearchCriteria("id", batchId, SearchOperation.EQUAL));
		} else {
			parentSpec.add(new SearchCriteria("id", 0, SearchOperation.GREATER_THAN));
		}

		GenericSpecification<BatchEvents> spec = new GenericSpecification<>();
		if (StringUtils.hasText(eventName)) {
			spec.add(new SearchCriteria("eventName", eventName, SearchOperation.EQUAL));
		}
		if (parentEventId > 0) {
			spec.add(new SearchCriteria("parentEventId", parentEventId, SearchOperation.EQUAL));
		}
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}

		ResultPage<BatchEvents> resultPage = null;
		if (pageSize > -1) {
			Page<BatchEvents> page = repository.findAll(spec.and(parentSpec), pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<BatchEvents> batchEventsList = repository.findAll(spec.and(parentSpec), pageable.getSort());
			resultPage = genericConversion.convertToResultData(batchEventsList);
		}
		
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<BatchEvents>> getAllBatchEvent() {
		logger.info("inside getAllBatchEvent...");
		List<BatchEvents> batchEventList = new ArrayList<>();
		try {
			batchEventList = (List<BatchEvents>) repository.findAll();
			if (batchEventList.isEmpty()) {
				logger.info("findAll(null): batchEvents");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(batchEventList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(batchEventList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<List<BatchEvents>> saveBatchEvents(Long batchId, List<Long> eventIdList) {
		logger.info("inside saveBatchEvents...");
		try {
			List<BatchEvents> batchEventsList= new ArrayList<>();
			repository.deactivateByBatchId(batchId);
			Batch batch = getBatch(batchId);
			List<Events> eventList = getEvents(eventIdList);
			for (Events event : eventList) {
				BatchEvents batchEvents = new BatchEvents();
				batchEvents.setBatch(batch);
				long id = getBatchEventCount();
				
				batchEvents.setEventName(event.getEventName());
				batchEvents.setDescription(event.getDescription());
				batchEvents.setId(++id);
				String timestamp = Util.getTimeStamp();
				batchEvents.setParentEventId(event.getId());
				batchEvents.setDateofEntry(Util.getDateOfEntry(timestamp));
				batchEvents.setStatus(ACTIVE_STATUS);
				batchEvents.setCreatedBy(Util.getDefaultUser());
				batchEvents.setCreatedAt(timestamp);
				batchEvents.setUpdatedBy(Util.getDefaultUser());
				batchEvents.setUpdatedAt(Util.getDefaultTimestamp());
				BatchEvents savedBatchEvents = repository.save(batchEvents);
				batchEventsList.add(savedBatchEvents);
			}
			return new ResponseEntity<>(batchEventsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("saveBatchEvents - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<BatchEvents> createBatchEvent(BatchEvents batchEvents) {
		logger.info("inside createBatchEvent...");
		try {
			Batch batch = getBatch(batchEvents.getBatchId());
			batchEvents.setBatch(batch);
			long id = getBatchEventCount();
			batchEvents.setId(++id);
			batchEvents.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			batchEvents.setDateofEntry(Util.getDateOfEntry(timestamp));
			batchEvents.setCreatedBy(Util.getDefaultUser());
			batchEvents.setCreatedAt(timestamp);
			batchEvents.setUpdatedBy(Util.getDefaultUser());
			batchEvents.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(batchEvents), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createBatchEvent - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<BatchEvents> updateBatchEvent(BatchEvents batchEvents) {
		logger.info("inside updateBatchEvent...");
		try {
			Optional<BatchEvents> optionalBatchEvent = repository.findById(batchEvents.getId());
			if (!optionalBatchEvent.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			BatchEvents existingBatchEvent = optionalBatchEvent.get();
			batchEvents.setCreatedBy(existingBatchEvent.getCreatedBy());
			batchEvents.setCreatedAt(existingBatchEvent.getCreatedAt());
			batchEvents.setUpdatedBy(Util.getDefaultUser());
			batchEvents.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(batchEvents), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update batch for " + batchEvents.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId...");
		return repository.count() + 1;
	}

	public BatchEvents getById(long id) {
		return repository.findById(id).orElseThrow(() -> new AppException("Entry not found for id: " + id));
	}
	
	private Batch getBatch(long batchId) {
		return batchService.getById(batchId);
	}
	
	private List<Events> getEvents(List<Long> eventIdList){
		return eventsService.getEvents(eventIdList);
	}
}