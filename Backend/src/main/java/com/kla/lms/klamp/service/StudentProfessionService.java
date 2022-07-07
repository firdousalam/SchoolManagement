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
import com.kla.lms.klamp.entity.StudentProfessionDetails;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudentProfessionDetailsRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudentProfessionService {
	public static final Logger logger = LoggerFactory.getLogger(StudentProfessionService.class);
	private static int ACTIVE_STATUS = 1;
	private static int PASSIVE_STATUS = 0;
	@Autowired
	StudentProfessionDetailsRepo studentProfessionRepo;

	@Autowired
	StudentProfileService studentProfileService;

	/*
	 * Get count of studentProfessions
	 */
	public long getStudentProfessionCount() {
		return studentProfessionRepo.count();
	}
	
	public List<StudentProfessionDetails> getStudentProfessionDetailsByProfileId(long profileId){
		logger.info("inside getStudentProfessionDetailsByProfileId..............");
		return  studentProfessionRepo.findStudentProfessionDetailsByProfileId(profileId);
		
	}

	public ResponseEntity<ResultPage<StudentProfessionDetails>> getStudentProfessionBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy,long profileId,
			String organization, String designation, String placeOfDuty, Integer status) {
		logger.info("inside getStudentProfessionBySearchCriteria...");
		GenericConversion<StudentProfessionDetails> genericConvertion = new GenericConversion<>();
		Pageable pageable = genericConvertion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<StudentProfile,StudentProfessionDetails> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("studentProfile");
		parentSpec.add(new SearchCriteria("profileId", profileId, SearchOperation.EQUAL));
		
		GenericSpecification<StudentProfessionDetails> spec = new GenericSpecification<>();
		if (StringUtils.hasText(organization)) {
			spec.add(new SearchCriteria("organization", organization, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(designation)) {
			spec.add(new SearchCriteria("designation", designation, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(placeOfDuty)) {
			spec.add(new SearchCriteria("placeOfDuty", placeOfDuty, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		Page<StudentProfessionDetails> page = studentProfessionRepo.findAll(parentSpec.and(spec),pageable);
		return new ResponseEntity<>(genericConvertion.convertToResultData(page), HttpStatus.OK);
	}

	public ResponseEntity<List<StudentProfessionDetails>> getAllStudentProfession() {
		List<StudentProfessionDetails> studentProfessionsList = new ArrayList<>();
		try {
			studentProfessionsList = (List<StudentProfessionDetails>) studentProfessionRepo.findAll();
			if (studentProfessionsList == null) {
				logger.info("findAll(null): {}");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(studentProfessionsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studentProfessionsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentProfessionDetails> createStudentProfession(
			StudentProfessionDetails studentProfession,long profileId) {
		try {
			long id = getStudentProfessionCount();
			studentProfession.setId(++id);
			studentProfession.setStudentProfile(studentProfileService.findProfileById(profileId));
			studentProfession.setStatus(ACTIVE_STATUS);
			studentProfession.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			studentProfession.setCreatedAt(Util.getTimeStamp());
			studentProfession.setUpdatedBy(Util.getDefaultUser());
			studentProfession.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<StudentProfessionDetails>(studentProfessionRepo.save(studentProfession),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentProfession for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<List<StudentProfessionDetails>> createStudentProfessionDetailsList(
			List<StudentProfessionDetails> studentProfessionDetailsList,long profileId) {
		List<StudentProfessionDetails> stProList = new ArrayList<>();
	    StudentProfile studentProfile = studentProfileService.findProfileById(profileId);
		long id = getStudentProfessionCount();
		try {
			for (StudentProfessionDetails sp : studentProfessionDetailsList) {
				sp.setId(++id);
				sp.setStudentProfile(studentProfile);
				sp.setStatus(ACTIVE_STATUS);
				sp.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
				sp.setCreatedAt(Util.getTimeStamp());
				sp.setUpdatedBy(Util.getDefaultUser());
				sp.setUpdatedAt(Util.getDefaultTimestamp());
				id = id + 1;
				stProList.add(sp);
			}
			System.out.println("============id=======" + id);

			return new ResponseEntity<List<StudentProfessionDetails>>(StreamSupport
					.stream(studentProfessionRepo.saveAll(stProList).spliterator(), false).collect(Collectors.toList()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentEducation for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentProfessionDetails> updateStudentProfession(
			StudentProfessionDetails studentProfession, long profileId) {
		try {
			StudentProfile studentProfile = studentProfileService
					.findProfileByUserId(studentProfession.getStudentProfile().getUserId());
			Optional<StudentProfessionDetails> optionalData = studentProfessionRepo.findById(studentProfession.getId());
			if (optionalData.isPresent()) {
				StudentProfessionDetails existingData = optionalData.get();
				studentProfession.setStudentProfile(studentProfile);
				existingData.setStatus(PASSIVE_STATUS);
				existingData.setUpdatedBy(Util.getDefaultUser());
				existingData.setUpdatedAt(Util.getTimeStamp());
				studentProfessionRepo.save(existingData);
			}
			studentProfession.setId(0);
			studentProfession.setUpdatedAt(null);
			studentProfession.setUpdatedAt(null);
			return createStudentProfession(studentProfession, profileId);

		} catch (Exception e) {
			logger.error("update studentProfessions for " + studentProfession.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		return studentProfessionRepo.count() + 1;
	}
}