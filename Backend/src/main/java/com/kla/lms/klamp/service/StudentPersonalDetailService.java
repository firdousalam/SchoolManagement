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

import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.StudentPersonalDetails;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudentPersonalDetailsRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudentPersonalDetailService {
	public static final Logger logger = LoggerFactory.getLogger(StudentPersonalDetailService.class);
	private static int ACTIVE_STATUS = 1;
	private static int PASSIVE_STATUS = 0;
	@Autowired
	StudentPersonalDetailsRepo studentPersonalDetailRepo;

	@Autowired
	StudentProfileService studentProfileService;

	/*
	 * Get count of studentPersonalDetails
	 */
	public long getStudentPersonalDetailCount() {
		return studentPersonalDetailRepo.count();
	}
	
	public StudentPersonalDetails getStudentPersonalDetailsByProfileId(long profileId){
		logger.info("inside getStudentPersonalDetailsByProfileId..............");
		return  studentPersonalDetailRepo.findStudentPersonalDetailsByProfileId(profileId);
		
	}

	public ResponseEntity<ResultPage<StudentPersonalDetails>> getStudentPersonalDetailBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy,long profileId,
		 String gender, String nationality, String scOrSt, String isEmployed, Integer status) {
		logger.info("inside getStudentPersonalDetailBySearchCriteria...");
		GenericConversion<StudentPersonalDetails> genericConvertion = new GenericConversion<>();
		Pageable pageable = genericConvertion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<StudentProfile,StudentPersonalDetails> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("studentProfile");
		parentSpec.add(new SearchCriteria("profileId", profileId, SearchOperation.EQUAL));
		
		GenericSpecification<StudentPersonalDetails> spec = new GenericSpecification<>();
		if (StringUtils.hasText(gender)) {
			spec.add(new SearchCriteria("gender", gender, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(nationality)) {
			spec.add(new SearchCriteria("nationality", nationality, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(scOrSt)) {
			spec.add(new SearchCriteria("scOrSt", scOrSt, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(isEmployed)) {
			spec.add(new SearchCriteria("isEmployed", isEmployed, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		Page<StudentPersonalDetails> page = studentPersonalDetailRepo.findAll(parentSpec.and(spec),pageable);
		return new ResponseEntity<>(genericConvertion.convertToResultData(page), HttpStatus.OK);
	}

	public ResponseEntity<List<StudentPersonalDetails>> getAllStudentPersonalDetail() {
		List<StudentPersonalDetails> studentPersonalDetailsList = new ArrayList<>();
		try {
			studentPersonalDetailsList = (List<StudentPersonalDetails>) studentPersonalDetailRepo.findAll();
			if (studentPersonalDetailsList == null) {
				logger.info("findAll(null): {}");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(studentPersonalDetailsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studentPersonalDetailsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentPersonalDetails> createStudentPersonalDetail(
			StudentPersonalDetails studentPersonalDetail,long profileId) {
		try {
			long id = getStudentPersonalDetailCount();
			studentPersonalDetail.setStudentProfile(studentProfileService.findProfileById(profileId));
			studentPersonalDetail.setId(++id);
			studentPersonalDetail.setStatus(ACTIVE_STATUS);
			studentPersonalDetail.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			studentPersonalDetail.setCreatedAt(Util.getTimeStamp());
			studentPersonalDetail.setUpdatedBy(Util.getDefaultUser());
			studentPersonalDetail.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<StudentPersonalDetails>(studentPersonalDetailRepo.save(studentPersonalDetail),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentPersonalDetail for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentPersonalDetails> updateStudentPersonalDetail(
			StudentPersonalDetails studentPersonalDetail, long profileId) {
		try {
			StudentProfile studentProfile = studentProfileService
					.findProfileByUserId(studentPersonalDetail.getStudentProfile().getUserId());
			Optional<StudentPersonalDetails> optionalData = studentPersonalDetailRepo
					.findById(studentPersonalDetail.getId());
			if (optionalData.isPresent()) {
				StudentPersonalDetails existingData = optionalData.get();
				studentPersonalDetail.setStudentProfile(studentProfile);
				existingData.setStatus(PASSIVE_STATUS);
				existingData.setUpdatedBy(Util.getDefaultUser());
				existingData.setUpdatedAt(Util.getTimeStamp());
				studentPersonalDetailRepo.save(existingData);
			}
			studentPersonalDetail.setId(0);
			studentPersonalDetail.setUpdatedAt(null);
			studentPersonalDetail.setUpdatedAt(null);
			return createStudentPersonalDetail(studentPersonalDetail,profileId);
		} catch (Exception e) {
			logger.error("update studentPersonalDetails for " + studentPersonalDetail.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		return studentPersonalDetailRepo.count() + 1;
	}
}