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
import com.kla.lms.klamp.entity.StudentDocuments;
import com.kla.lms.klamp.entity.StudentProfile;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudentDocumentsRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudentDocumentService {
	public static final Logger logger = LoggerFactory.getLogger(StudentDocumentService.class);
	private static int ACTIVE_STATUS = 1;
	private static int PASSIVE_STATUS = 0;
	@Autowired
	StudentDocumentsRepo studentDocumentRepo;

	@Autowired
	StudentProfileService studentProfileService;

	/*
	 * Get count of studentDocuments
	 */
	public long getStudentDocumentCount() {
		return studentDocumentRepo.count();
	}
	
	public List<StudentDocuments> getStudentDocumentsByProfileId(long profileId){
		logger.info("inside getStudentDocumentsByProfileId..............");
		return  studentDocumentRepo.findStudentDocumentsByProfileId(profileId);
		
	}

	public ResponseEntity<ResultPage<StudentDocuments>> getStudentDocumentBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy,long profileId,
			String documentType, String documentName, Integer status) {
		logger.info("inside getStudentDocumentBySearchCriteria...");
		GenericConversion<StudentDocuments> genericConvertion = new GenericConversion<>();
		Pageable pageable = genericConvertion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<StudentProfile,StudentDocuments> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("studentProfile");
		parentSpec.add(new SearchCriteria("profileId", profileId, SearchOperation.EQUAL));
		
		GenericSpecification<StudentDocuments> spec = new GenericSpecification<>();
		if (StringUtils.hasText(documentType)) {
			spec.add(new SearchCriteria("documentType", documentType, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(documentName)) {
			spec.add(new SearchCriteria("documentName", documentName, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		Page<StudentDocuments> page = studentDocumentRepo.findAll(parentSpec.and(spec),pageable);
		return new ResponseEntity<>(genericConvertion.convertToResultData(page), HttpStatus.OK);
	}

	public ResponseEntity<List<StudentDocuments>> getAllStudentDocument() {
		List<StudentDocuments> studentDocumentsList = new ArrayList<>();
		try {
			studentDocumentsList = (List<StudentDocuments>) studentDocumentRepo.findAll();
			if (studentDocumentsList == null) {
				logger.info("findAll(null): {}");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(studentDocumentsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studentDocumentsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentDocuments> createStudentDocument(StudentDocuments studentDocument,long profileId) {
		try {
			long id = getStudentDocumentCount();
			studentDocument.setId(++id);
			studentDocument.setStudentProfile(studentProfileService.findProfileById(profileId));
			studentDocument.setStatus(ACTIVE_STATUS);
			studentDocument.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			studentDocument.setCreatedAt(Util.getTimeStamp());
			studentDocument.setUpdatedBy(Util.getDefaultUser());
			studentDocument.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<StudentDocuments>(studentDocumentRepo.save(studentDocument), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentDocument for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<List<StudentDocuments>> createStudentDocumentsList(
			List<StudentDocuments> studentDocumentsList,long profileId) {
		List<StudentDocuments> stDocList = new ArrayList<>();
		StudentProfile studentProfile = studentProfileService.findProfileById(profileId);
		long id = getStudentDocumentCount();
		try {
			for (StudentDocuments sd : studentDocumentsList) {
				sd.setId(++id);
				sd.setStudentProfile(studentProfile);
				sd.setStatus(ACTIVE_STATUS);
				sd.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
				sd.setCreatedAt(Util.getTimeStamp());
				sd.setUpdatedBy(Util.getDefaultUser());
				sd.setUpdatedAt(Util.getDefaultTimestamp());
				id = id + 1;
				stDocList.add(sd);
			}
			System.out.println("============id=======" + id);

			return new ResponseEntity<List<StudentDocuments>>(StreamSupport
					.stream(studentDocumentRepo.saveAll(stDocList).spliterator(), false).collect(Collectors.toList()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudentEducation for unsuccessfull====== ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudentDocuments> updateStudentDocument(StudentDocuments studentDocument, long profileId) {
		try {
			StudentProfile studentProfile = studentProfileService
					.findProfileByUserId(studentDocument.getStudentProfile().getUserId());
			Optional<StudentDocuments> optionalData = studentDocumentRepo.findById(studentDocument.getId());
			if (optionalData.isPresent()) {
				StudentDocuments existingData = optionalData.get();
				studentDocument.setStudentProfile(studentProfile);
				existingData.setStatus(PASSIVE_STATUS);
				existingData.setUpdatedBy(Util.getDefaultUser());
				existingData.setUpdatedAt(Util.getTimeStamp());
				studentDocumentRepo.save(existingData);
			}
			studentDocument.setId(0);
			studentDocument.setUpdatedAt(null);
			studentDocument.setUpdatedAt(null);
			return createStudentDocument(studentDocument,profileId);

		} catch (Exception e) {
			logger.error("update studentDocuments for " + studentDocument.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		return studentDocumentRepo.count() + 1;
	}
}