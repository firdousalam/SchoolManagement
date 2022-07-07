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

import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.StudentContactDetails;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudentContactDetailsRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudentContactDetailService {
	public static final Logger logger = LoggerFactory.getLogger(StudentContactDetailService.class);
	private static int ACTIVE_STATUS = 1;
	private static int PASSIVE_STATUS = 0;
	@Autowired
	StudentContactDetailsRepo studentContactDetailRepo;

	@Autowired
	StudentProfileService studentProfileService;

	/*
	 * Get count of studentContactDetails
	 */
	public long getStudentContactDetailCount() {
		return studentContactDetailRepo.count();
	}
	
	public List<StudentContactDetails> getStudentContactDetailsByProfileId(long profileId){
		logger.info("inside getStudentContactDetailsByProfileId..............");
		return  studentContactDetailRepo.findStudentContactDetailsByProfileId(profileId);
		
	}

	public ResponseEntity<ResultPage<StudentContactDetails>> getStudentContactDetailBySearchCriteria(Integer pageNumber,
			Integer pageSize, String sortDirection, String sortBy, long profileId, String mobileNumber, String emailId,
			Integer status) {
		logger.info("inside getStudentContactDetailBySearchCriteria...");
		GenericConversion<StudentContactDetails> genericConvertion = new GenericConversion<>();
		Pageable pageable = genericConvertion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<StudentProfile, StudentContactDetails> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("studentProfile");
		parentSpec.add(new SearchCriteria("profileId", profileId, SearchOperation.EQUAL));

		GenericSpecification<StudentContactDetails> spec = new GenericSpecification<>();
		if (StringUtils.hasText(mobileNumber)) {
			spec.add(new SearchCriteria("mobileNumber", mobileNumber, SearchOperation.EQUAL));
		}

		if (StringUtils.hasText(emailId)) {
			spec.add(new SearchCriteria("emailId", emailId, SearchOperation.EQUAL));
		}

		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}

		Page<StudentContactDetails> page = studentContactDetailRepo.findAll(parentSpec.and(spec), pageable);
		return new ResponseEntity<>(genericConvertion.convertToResultData(page), HttpStatus.OK);
	}

	public ResponseEntity<List<StudentContactDetails>> getAllStudentContactDetail() {
		List<StudentContactDetails> studentContactDetailsList = new ArrayList<>();
		try {
			studentContactDetailsList = (List<StudentContactDetails>) studentContactDetailRepo.findAll();
			if (studentContactDetailsList == null) {
				logger.info("findAll(null): {}");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(studentContactDetailsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studentContactDetailsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentContactDetails> createStudentContactDetail(
			StudentContactDetails studentContactDetail,long profileId) {
		try {
			long id = getStudentContactDetailCount();
			studentContactDetail.setId(++id);
			studentContactDetail.setStudentProfile(studentProfileService.findProfileById(profileId));
			studentContactDetail.setStatus(ACTIVE_STATUS);
			studentContactDetail.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			studentContactDetail.setCreatedAt(Util.getTimeStamp());
			studentContactDetail.setUpdatedBy(Util.getDefaultUser());
			studentContactDetail.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<StudentContactDetails>(studentContactDetailRepo.save(studentContactDetail),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentContactDetail for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	public ResponseEntity<List<StudentContactDetails>> createStudentContactDetailsList(
			List<StudentContactDetails> studentContactDetailsList,long profileId) {
		List<StudentContactDetails> stConList = new ArrayList<>();
		long id = getStudentContactDetailCount();
		StudentProfile studentProfile = studentProfileService.findProfileById(profileId);
		try {
			for (StudentContactDetails sc : studentContactDetailsList) {
				sc.setId(++id);
				sc.setStudentProfile(studentProfile);
				sc.setStatus(ACTIVE_STATUS);
				sc.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
				sc.setCreatedAt(Util.getTimeStamp());
				sc.setUpdatedBy(Util.getDefaultUser());
				sc.setUpdatedAt(Util.getDefaultTimestamp());
				id = id + 1;
				stConList.add(sc);
			}
			System.out.println("============id=======" + id);

			return new ResponseEntity<List<StudentContactDetails>>(StreamSupport
					.stream(studentContactDetailRepo.saveAll(stConList).spliterator(), false).collect(Collectors.toList()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentEducation for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	public ResponseEntity<StudentContactDetails> updateStudentContactDetail(
			StudentContactDetails studentContactDetail, long profileId) {
		try {
			StudentProfile studentProfile = studentProfileService
					.findProfileByUserId(studentContactDetail.getStudentProfile().getUserId());
			Optional<StudentContactDetails> optionalData = studentContactDetailRepo
					.findById(studentContactDetail.getId());
			if (optionalData.isPresent()) {
				StudentContactDetails existingData = optionalData.get();
				studentContactDetail.setStudentProfile(studentProfile);
				existingData.setStatus(PASSIVE_STATUS);
				existingData.setUpdatedBy(Util.getDefaultUser());
				existingData.setUpdatedAt(Util.getTimeStamp());
				studentContactDetailRepo.save(existingData);
			}
			studentContactDetail.setId(0);
			studentContactDetail.setUpdatedAt(null);
			studentContactDetail.setUpdatedAt(null);
			return createStudentContactDetail(studentContactDetail,profileId);
		} catch (Exception e) {
			logger.error("update studentContactDetails for " + studentContactDetail.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		return studentContactDetailRepo.count() + 1;
	}
}