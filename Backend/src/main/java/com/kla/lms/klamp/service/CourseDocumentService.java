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
import com.kla.lms.klamp.entity.CourseDocument;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.CourseDocumentRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class CourseDocumentService {
	public static final Logger logger = LoggerFactory.getLogger(CourseDocumentService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;
	
	@Autowired
	CourseDocumentRepo repository;

	public long getCourseDocumentCount() {
		logger.info("inside getCourseDocumentCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<CourseDocument>> getCourseDocumentBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String documentType, long batchId,Integer status) {
		logger.info("inside getCourseDocumentBySearchCriteria..............");
		GenericConversion<CourseDocument> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<Batch,CourseDocument> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batch");
		parentSpec.add(new SearchCriteria("batchId", batchId, SearchOperation.EQUAL));
		
		GenericSpecification<CourseDocument> spec = new GenericSpecification<>();
		if (StringUtils.hasText(documentType)) {
			spec.add(new SearchCriteria("documentType", documentType, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<CourseDocument> resultPage = null;
		if (pageSize > -1) {
			Page<CourseDocument> page = repository.findAll(parentSpec.and(spec),pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<CourseDocument> courseDocumentList = repository.findAll(parentSpec.and(spec),pageable.getSort());
			resultPage = genericConversion.convertToResultData(courseDocumentList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<CourseDocument>> getAllCourseDocument() {
		logger.info("inside getAllCourseDocument..............");
		List<CourseDocument> courseDocumentsList = new ArrayList<>();
		try {
			courseDocumentsList = (List<CourseDocument>) repository.findAll();
			if (courseDocumentsList.isEmpty()) {
				logger.info("findAll(null): courseCalendar");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(courseDocumentsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(courseDocumentsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<CourseDocument> createCourseDocument(CourseDocument courseDocument) {
		logger.info("inside createCourseDocument..............");
		try {
			long id = getCourseDocumentCount();
			courseDocument.setId(++id);
			courseDocument.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			courseDocument.setDateofEntry(Util.getDateOfEntry(timestamp));
			courseDocument.setCreatedBy(Util.getDefaultUser());
			courseDocument.setCreatedAt(timestamp);
			courseDocument.setUpdatedBy(Util.getDefaultUser());
			courseDocument.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(courseDocument), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createCourseDocument - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<CourseDocument> updateCourseDocument(CourseDocument courseDocument) {
		logger.info("inside updateCourseDocument..............");
		try {
			Optional<CourseDocument> optionalCourseDocument = repository.findById(courseDocument.getId());
			if (optionalCourseDocument.isPresent()) {
				CourseDocument existingCourseDocument = optionalCourseDocument.get();
				existingCourseDocument.setStatus(PASSIVE_STATUS);
				existingCourseDocument.setUpdatedBy(Util.getDefaultUser());
				existingCourseDocument.setUpdatedAt(Util.getTimeStamp());
				repository.save(existingCourseDocument);
			}
			courseDocument.setId(0);
			courseDocument.setUpdatedAt(null);
			courseDocument.setUpdatedAt(null);
			return createCourseDocument(courseDocument);

		} catch (Exception e) {
			logger.error("update courseDocuments for " + courseDocument.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
}