package com.kla.lms.klamp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kla.lms.klamp.entity.Application;
import com.kla.lms.klamp.entity.Batch;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.StudentPersonalDetails;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.ApplicationRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class ApplicationService {
	public static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;

	@Autowired
	ApplicationRepo repository;
	
	@Autowired
	StudentProfileService studentProfileService;
	
	public List<Application> getApplicationByProfileId(long profileId){
		logger.info("inside getApplicationByProfileId..............");
		return  repository.findApplicationByProfileId(profileId);
		
	}

	public long getApplicationCount() {
		logger.info("inside getApplicationCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Application>> getApplicationBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String applicationState, long batchId, long studentId,
			Integer status) {
		logger.info("inside getApplicationBySearchCriteria..............");

		GenericConversion<Application> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		GenericJoinSpecification<Batch, Application> batchSpec = new GenericJoinSpecification<>();
		batchSpec.setParentName("batch");
		batchSpec.add(new SearchCriteria("id", batchId, SearchOperation.EQUAL));

		GenericJoinSpecification<StudentPersonalDetails, Application> studentSpec = new GenericJoinSpecification<>();
		studentSpec.setParentName("studentPersonalDetails");
		studentSpec.add(new SearchCriteria("studentId", studentId, SearchOperation.EQUAL));
		
		GenericSpecification<Application> appSpec = new GenericSpecification<>();
		if (StringUtils.hasText(applicationState)) {
			appSpec.add(new SearchCriteria("applicationState", applicationState, SearchOperation.EQUAL));
		}
		if (status != null && status > -1) {
			appSpec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}

		ResultPage<Application> resultPage = null;
		if (pageSize > -1) {
			Page<Application> page = repository.findAll(batchSpec.and(studentSpec).and(appSpec), pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Application> applicationList = repository.findAll(batchSpec.and(studentSpec).and(appSpec), pageable.getSort());
			resultPage = genericConversion.convertToResultData(applicationList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Application>> getAllApplication() {
		logger.info("inside getAllApplication..............");
		List<Application> applicationsList = new ArrayList<>();
		try {
			applicationsList = (List<Application>) repository.findAll();
			if (applicationsList.isEmpty()) {
				logger.info("findAll(null): application");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(applicationsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(applicationsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Application> createApplication(Application application) {
		logger.info("inside updateApplication..............");
		try {
			long id = getApplicationCount();
			application.setId(++id);
			application.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			application.setDateofEntry(Util.getDateOfEntry(timestamp));
			application.setCreatedBy(Util.getDefaultUser());
			application.setCreatedAt(timestamp);
			application.setUpdatedBy(Util.getDefaultUser());
			application.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(application), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createApplication - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Application> updateApplication(Application application) {
		logger.info("inside updateApplication..............");
		try {
			Optional<Application> optionalApplication = repository.findById(application.getId());
			
			if (optionalApplication.isPresent()) {
				Application existingApplication = optionalApplication.get();
				existingApplication.setStatus(PASSIVE_STATUS);
				existingApplication.setUpdatedBy(Util.getDefaultUser());
				existingApplication.setUpdatedAt(Util.getTimeStamp());
				repository.save(existingApplication);
			}
			application.setId(0);
			application.setUpdatedAt(null);
			application.setUpdatedAt(null);
			return createApplication(application);

		} catch (Exception e) {
			logger.error("update applications for " + application.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
	public ResponseEntity<List<Application>> createApplicationList(List<Application> applicationList,long profileId) {
		logger.info("inside updateApplication..............");
		List<Application> appList = new ArrayList<>();
		long id = getApplicationCount();
		StudentProfile studentProfile = studentProfileService.findProfileById(profileId);
		try {
			for(Application appObj : applicationList) {
				appObj.setId(++id);
				appObj.setStudentProfile(studentProfile);
				appObj.setStatus(ACTIVE_STATUS);
				appObj.setCreatedBy(Util.getDefaultUser());
				appObj.setCreatedAt(Util.getTimeStamp());
				appObj.setUpdatedBy(Util.getDefaultUser());
				appObj.setUpdatedAt(Util.getDefaultTimestamp());
				id = id + 1;
				appList.add(appObj);
			}
			return new ResponseEntity<List<Application>>(StreamSupport
					.stream(repository.saveAll(appList).spliterator(), false).collect(Collectors.toList()), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createApplication - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}