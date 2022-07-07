package com.kla.lms.klamp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudentProfileRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudentProfileService {
	public static final Logger logger = LoggerFactory.getLogger(StudentProfileService.class);
	private static int ACTIVE_STATUS = 1;
	private static int PASSIVE_STATUS = 0;
	@Autowired
	StudentProfileRepo studentProfileRepo;

	@Autowired
	StudentContactDetailService studentContactDetailService;

	@Autowired
	StudentDocumentService studentDocumentService;

	@Autowired
	StudentEducationDetailService studentEducationDetailService;

	@Autowired
	StudentPersonalDetailService studentPersonalDetailService;

	@Autowired
	StudentProfessionService studentProfessionService;

	@Autowired
	ApplicationService applicationService;

	/*
	 * Get count of student Profile
	 */
	public long getStudentProfileCount() {
		return studentProfileRepo.count();
	}

	public StudentProfile findProfileByUserId(long userId) {
		return studentProfileRepo.findProfileByUserId(userId);
	}

	public StudentProfile findProfileById(long profileId) {
		return studentProfileRepo.findById(profileId).get();
	}

	public ResponseEntity<ResultPage<StudentProfile>> getStudentProfileBySearchCriteria(Integer pageNumber,
			Integer pageSize, String sortDirection, String sortBy, String userId, Long profileId,Integer status) {
		logger.info("inside getStudentProfileBySearchCriteria..............");
		GenericSpecification<StudentProfile> genericSpecification = new GenericSpecification<>();
		GenericConversion<StudentProfile> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);

		if (StringUtils.hasText(userId)) {
			genericSpecification.add(new SearchCriteria("userId", userId, SearchOperation.EQUAL));
		}
		
		if (profileId > 0) {
			genericSpecification.add(new SearchCriteria("id", profileId, SearchOperation.EQUAL));
		}

		if (status != null && status > -1) {
			genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}

		Page<StudentProfile> page = studentProfileRepo.findAll(genericSpecification, pageable);
		List<StudentProfile> studentProfilesList = page.getContent();
		List<StudentProfile> studentProfilesResponseList = new ArrayList<>();
		for(StudentProfile stdProfile : studentProfilesList) {
			stdProfile.setApplicationList(applicationService.getApplicationByProfileId(stdProfile.getId()));
			stdProfile.setStudentContactDetailsList(studentContactDetailService.getStudentContactDetailsByProfileId(stdProfile.getId()));
			stdProfile.setStudentDocumentsList(studentDocumentService.getStudentDocumentsByProfileId(stdProfile.getId()));
			stdProfile.setStudentEducationDetailsList(studentEducationDetailService.getStudentEducationsByProfileId(stdProfile.getId()));
			stdProfile.setStudentPersonalDetails(studentPersonalDetailService.getStudentPersonalDetailsByProfileId(stdProfile.getId()));
			stdProfile.setStudentProfessionalDetailsList(studentProfessionService.getStudentProfessionDetailsByProfileId(stdProfile.getId()));
			studentProfilesResponseList.add(stdProfile);
		}
		page = new PageImpl<>(studentProfilesResponseList, pageable, pageSize);
		return new ResponseEntity<>(genericConversion.convertToResultData(page), HttpStatus.OK);
	}

	public ResponseEntity<List<StudentProfile>> getAllStudentProfile() {
		logger.info("inside getAllStudentProfile..............");
		List<StudentProfile> studentProfilesList = new ArrayList<>();
		List<StudentProfile> studentProfilesResponseList = new ArrayList<>();
		try {
			studentProfilesList = (List<StudentProfile>) studentProfileRepo.findAll();
			if (studentProfilesList == null) {
				logger.info("findAll(null): {}");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				for(StudentProfile stdProfile : studentProfilesList) {
					stdProfile.setApplicationList(applicationService.getApplicationByProfileId(stdProfile.getId()));
					stdProfile.setStudentContactDetailsList(studentContactDetailService.getStudentContactDetailsByProfileId(stdProfile.getId()));
					stdProfile.setStudentDocumentsList(studentDocumentService.getStudentDocumentsByProfileId(stdProfile.getId()));
					stdProfile.setStudentEducationDetailsList(studentEducationDetailService.getStudentEducationsByProfileId(stdProfile.getId()));
					stdProfile.setStudentPersonalDetails(studentPersonalDetailService.getStudentPersonalDetailsByProfileId(stdProfile.getId()));
					stdProfile.setStudentProfessionalDetailsList(studentProfessionService.getStudentProfessionDetailsByProfileId(stdProfile.getId()));
					studentProfilesResponseList.add(stdProfile);
				}
			}

			return new ResponseEntity<>(studentProfilesResponseList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studentProfilesList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentProfile> createStudentProfile(StudentProfile StudentProfile) {
		logger.info("inside createStudentProfile..............");
		try {
			System.out.println("===========userId=============" + StudentProfile.getUserId());
			StudentProfile requestObj = StudentProfile;
			StudentProfile studentObj = new StudentProfile();
			long id = getStudentProfileCount();
			StudentProfile.setId(++id);
			StudentProfile.setStatus(ACTIVE_STATUS);
			StudentProfile.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			StudentProfile.setCreatedAt(Util.getTimeStamp());
			StudentProfile.setUpdatedBy(Util.getDefaultUser());
			StudentProfile.setUpdatedAt(Util.getDefaultTimestamp());
			studentObj = studentProfileRepo.save(StudentProfile);
			studentObj.setStudentContactDetailsList(studentContactDetailService
					.createStudentContactDetailsList(requestObj.getStudentContactDetailsList(), studentObj.getId())
					.getBody());
			studentObj.setStudentDocumentsList(studentDocumentService
					.createStudentDocumentsList(requestObj.getStudentDocumentsList(), studentObj.getId()).getBody());
			studentObj.setStudentEducationDetailsList(studentEducationDetailService
					.createStudentEducationList(requestObj.getStudentEducationDetailsList(), studentObj.getId())
					.getBody());
			studentObj.setStudentPersonalDetails(studentPersonalDetailService
					.createStudentPersonalDetail(requestObj.getStudentPersonalDetails(), studentObj.getId()).getBody());
			studentObj.setStudentProfessionalDetailsList(studentProfessionService.createStudentProfessionDetailsList(
					requestObj.getStudentProfessionalDetailsList(), studentObj.getId()).getBody());
			studentObj.setApplicationList(applicationService
					.createApplicationList(requestObj.getApplicationList(), studentObj.getId()).getBody());
			return new ResponseEntity<StudentProfile>(studentObj, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentProfile for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentProfile> updateStudentProfile(StudentProfile studentPersonalDetail) {
		logger.info("inside updateStudentProfile..............");
		try {
			StudentProfile existingStudentProfiles = studentProfileRepo.findById(studentPersonalDetail.getId()).get();
			existingStudentProfiles.setStatus(PASSIVE_STATUS);
			existingStudentProfiles.setUpdatedBy(Util.getDefaultUser());
			existingStudentProfiles.setUpdatedAt(Util.getTimeStamp());
			studentProfileRepo.save(existingStudentProfiles);
			studentPersonalDetail.setId(0);
			studentPersonalDetail.setUpdatedAt(null);
			studentPersonalDetail.setUpdatedAt(null);
			return createStudentProfile(studentPersonalDetail);

		} catch (Exception e) {
			logger.error("update studentPersonalDetails for " + studentPersonalDetail.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return studentProfileRepo.count() + 1;
	}
}