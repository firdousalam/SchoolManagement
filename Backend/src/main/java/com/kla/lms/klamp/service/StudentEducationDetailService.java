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
import com.kla.lms.klamp.entity.StudentEducations;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudentEducationRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudentEducationDetailService {
	public static final Logger logger = LoggerFactory.getLogger(StudentEducationDetailService.class);
	private static int ACTIVE_STATUS = 1;
	private static int PASSIVE_STATUS = 0;
	@Autowired
	StudentEducationRepo studentEducationRepo;

	@Autowired
	StudentProfileService studentProfileService;

	/*
	 * Get count of studentEducations
	 */
	public long getStudentEducationCount() {
		return studentEducationRepo.count();
	}
	
	public List<StudentEducations> getStudentEducationsByProfileId(long profileId){
		logger.info("inside getStudentEducationsByProfileId..............");
		return  studentEducationRepo.findStudentEducationsByProfileId(profileId);
		
	}

	public ResponseEntity<ResultPage<StudentEducations>> getStudentEducationBySearchCriteria(Integer pageNumber,
			Integer pageSize, String sortDirection, String sortBy, long profileId, String courseName,
			String institution, String educationStatus, String yearofCompletion, String percentage, Integer status) {
		logger.info("inside getStudentEducationBySearchCriteria...");
		GenericConversion<StudentEducations> genericConvertion = new GenericConversion<>();
		Pageable pageable = genericConvertion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<StudentProfile, StudentEducations> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("studentProfile");
		parentSpec.add(new SearchCriteria("profileId", profileId, SearchOperation.EQUAL));

		GenericSpecification<StudentEducations> spec = new GenericSpecification<>();
		if (StringUtils.hasText(courseName)) {
			spec.add(new SearchCriteria("courseName", courseName, SearchOperation.EQUAL));
		}

		if (StringUtils.hasText(institution)) {
			spec.add(new SearchCriteria("institution", institution, SearchOperation.EQUAL));
		}

		if (StringUtils.hasText(educationStatus)) {
			spec.add(new SearchCriteria("educationStatus", educationStatus, SearchOperation.EQUAL));
		}

		if (StringUtils.hasText(yearofCompletion)) {
			spec.add(new SearchCriteria("yearofCompletion", yearofCompletion, SearchOperation.EQUAL));
		}

		if (StringUtils.hasText(percentage)) {
			spec.add(new SearchCriteria("percentage", percentage, SearchOperation.EQUAL));
		}

		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}

		Page<StudentEducations> page = studentEducationRepo.findAll(parentSpec.and(spec), pageable);
		return new ResponseEntity<>(genericConvertion.convertToResultData(page), HttpStatus.OK);

	}

	public ResponseEntity<List<StudentEducations>> getAllStudentEducation() {
		List<StudentEducations> studentEducationsList = new ArrayList<>();
		try {
			studentEducationsList = (List<StudentEducations>) studentEducationRepo.findAll();
			if (studentEducationsList == null) {
				logger.info("findAll(null): {}");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(studentEducationsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studentEducationsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentEducations> createStudentEducation(StudentEducations studentEducation,long profileId) {
		try {
			long id = getStudentEducationCount();
			studentEducation.setId(++id);
			studentEducation.setStudentProfile(studentProfileService.findProfileById(profileId));
			studentEducation.setStatus(ACTIVE_STATUS);
			studentEducation.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			studentEducation.setCreatedAt(Util.getTimeStamp());
			studentEducation.setUpdatedBy(Util.getDefaultUser());
			studentEducation.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<StudentEducations>(studentEducationRepo.save(studentEducation),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentEducation for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<List<StudentEducations>> createStudentEducationList(
			List<StudentEducations> studentEducationList,long profileId) {
		List<StudentEducations> stEduList = new ArrayList<>();
		long id = getStudentEducationCount();
		StudentProfile studentProfile = studentProfileService.findProfileById(profileId);
		try {
			for (StudentEducations se : studentEducationList) {
				se.setId(++id);
				se.setStudentProfile(studentProfile);
				se.setStatus(ACTIVE_STATUS);
				se.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
				se.setCreatedAt(Util.getTimeStamp());
				se.setUpdatedBy(Util.getDefaultUser());
				se.setUpdatedAt(Util.getDefaultTimestamp());
				id = id + 1;
				stEduList.add(se);
			}
			System.out.println("============student education id=======" + id);

			return new ResponseEntity<List<StudentEducations>>(StreamSupport
					.stream(studentEducationRepo.saveAll(stEduList).spliterator(), false).collect(Collectors.toList()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentEducation for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentEducations> updateStudentEducation(StudentEducations studentEducation, long profileId) {
		try {
			StudentProfile studentProfile = studentProfileService
					.findProfileByUserId(studentEducation.getStudentProfile().getUserId());
			Optional<StudentEducations> optionalData = studentEducationRepo.findById(studentEducation.getId());
			if (optionalData.isPresent()) {
				StudentEducations existingData = optionalData.get();
				studentEducation.setStudentProfile(studentProfile);
				existingData.setStatus(PASSIVE_STATUS);
				existingData.setUpdatedBy(Util.getDefaultUser());
				existingData.setUpdatedAt(Util.getTimeStamp());
				studentEducationRepo.save(existingData);
			}
			studentEducation.setId(0);
			studentEducation.setUpdatedAt(null);
			studentEducation.setUpdatedAt(null);
			return createStudentEducation(studentEducation, profileId);

		} catch (Exception e) {
			logger.error("update studentEducations for " + studentEducation.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		return studentEducationRepo.count() + 1;
	}
}