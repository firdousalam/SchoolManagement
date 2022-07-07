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

import com.kla.lms.klamp.entity.Batch;
import com.kla.lms.klamp.entity.CourseCalendar;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.CourseCalendarRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class CourseCalendarService {
	public static final Logger logger = LoggerFactory.getLogger(CourseCalendarService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;
	
	@Autowired
	CourseCalendarRepo repository;

	public long getCourseCalendarCount() {
		logger.info("inside getCourseCalendarCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<CourseCalendar>> getCourseCalendarBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy,long batchId, Integer status) {
		logger.info("inside getCourseCalendarBySearchCriteria..............");
		GenericConversion<CourseCalendar> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<Batch,CourseCalendar> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batch");
		parentSpec.add(new SearchCriteria("batchId", batchId, SearchOperation.EQUAL));
		
		GenericSpecification<CourseCalendar> spec = new GenericSpecification<>();
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<CourseCalendar> resultPage = null;
		if (pageSize > -1) {
			Page<CourseCalendar> page = repository.findAll(spec, pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<CourseCalendar> courseCalendarList = repository.findAll(spec, pageable.getSort());
			resultPage = genericConversion.convertToResultData(courseCalendarList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<CourseCalendar>> getAllCourseCalendar() {
		logger.info("inside getAllCourseCalendar..............");
		List<CourseCalendar> courseCalendarList = new ArrayList<>();
		try {
			courseCalendarList = (List<CourseCalendar>) repository.findAll();
			if (courseCalendarList.isEmpty()) {
				logger.info("findAll(null): courseCalendar");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(courseCalendarList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(courseCalendarList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<CourseCalendar> createCourseCalendar(CourseCalendar courseCalendar) {
		logger.info("inside createCourseCalendar..............");
		try {
			long id = getCourseCalendarCount();
			courseCalendar.setId(++id);
			courseCalendar.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			courseCalendar.setDateofEntry(Util.getDateOfEntry(timestamp));
			courseCalendar.setCreatedBy(Util.getDefaultUser());
			courseCalendar.setCreatedAt(timestamp);
			courseCalendar.setUpdatedBy(Util.getDefaultUser());
			courseCalendar.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(courseCalendar), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createCourseCalendar - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<CourseCalendar> updateCourseCalendar(CourseCalendar courseCalendar) {
		logger.info("inside updateCourseCalendar..............");
		try {
			Optional<CourseCalendar> optionalCourseCalendar = repository.findById(courseCalendar.getId());
			if (optionalCourseCalendar.isPresent()) {
				CourseCalendar existingCourseCalendar = optionalCourseCalendar.get();
				existingCourseCalendar.setStatus(PASSIVE_STATUS);
				existingCourseCalendar.setUpdatedBy(Util.getDefaultUser());
				existingCourseCalendar.setUpdatedAt(Util.getTimeStamp());
				repository.save(existingCourseCalendar);
			}
			courseCalendar.setId(0);
			courseCalendar.setUpdatedAt(null);
			courseCalendar.setUpdatedAt(null);
			return createCourseCalendar(courseCalendar);

		} catch (Exception e) {
			logger.error("update courseCalendar for " + courseCalendar.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
}