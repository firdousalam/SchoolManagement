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

import com.kla.lms.klamp.entity.Events;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.EventsRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class EventsService {
	public static final Logger logger = LoggerFactory.getLogger(EventsService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	EventsRepo repository;

	public long getEventsCount() {
		logger.info("inside getEventsCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Events>> getEventsBySearchCriteria(int pageNumber, int pageSize, String sortDirection,
			String sortBy, String eventName, Integer status) {
		logger.info("inside getEventsBySearchCriteria..............");
		GenericConversion<Events> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		GenericSpecification<Events> genericSpecification = new GenericSpecification<>();
		if (StringUtils.hasText(eventName)) {
			genericSpecification.add(new SearchCriteria("eventName", eventName, SearchOperation.EQUAL));
		}
		
		if (status != null && status>-1) {
			genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Events> resultPage = null;
		if (pageSize > -1) {
			Page<Events> page = repository.findAll(genericSpecification, pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Events> eventsList = repository.findAll(genericSpecification, pageable.getSort());
			resultPage = genericConversion.convertToResultData(eventsList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Events>> getAllEvents() {
		logger.info("inside getAllEvents..............");
		List<Events> eventsList = new ArrayList<>();
		try {
			eventsList = (List<Events>) repository.findAll();
			if (eventsList.isEmpty()) {
				logger.info("findAll(null): events");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(eventsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(eventsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Events> createEvents(Events event) {
		logger.info("inside createEvents..............");
		try {
			long id = getEventsCount();
			event.setId(++id);
			event.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			event.setDateofEntry(Util.getDateOfEntry(timestamp));
			event.setCreatedBy(Util.getDefaultUser());
			event.setCreatedAt(timestamp);
			event.setUpdatedBy(Util.getDefaultUser());
			event.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(event), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createEvent - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Events> updateEvents(Events event) {
		logger.info("inside updateEvents..............");
		try {
			Optional<Events> optionalEvents = repository.findById(event.getId());
			if (!optionalEvents.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Events existingEvents = optionalEvents.get();
			event.setCreatedAt(existingEvents.getCreatedAt());
			event.setCreatedBy(existingEvents.getCreatedBy());
			event.setUpdatedBy(Util.getDefaultUser());
			event.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(event), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update events for " + event.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
	
	public List<Events> getEvents(List<Long> idList){
		GenericSpecification<Events> genericSpecification = new GenericSpecification<>();
		genericSpecification.add(new SearchCriteria("id", idList.toArray(), SearchOperation.IN));
		return repository.findAll(genericSpecification);
	}
}