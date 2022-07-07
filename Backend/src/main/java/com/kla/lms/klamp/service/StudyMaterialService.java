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
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.StudyMaterial;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.StudyMaterialRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class StudyMaterialService {
	public static final Logger logger = LoggerFactory.getLogger(StudyMaterialService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	StudyMaterialRepo repository;

	public long getStudyMaterialCount() {
		logger.info("inside getStudyMaterialCount...");
		return repository.count();
	}

	public ResponseEntity<ResultPage<StudyMaterial>> getStudyMaterialBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy,
			String heading, String subject, long batchId,Integer status) {
		logger.info("inside getStudyMaterialBySearchCriteria..............");
		GenericConversion<StudyMaterial> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<Batch,StudyMaterial> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batch");
		
		if (batchId > 0) {
			parentSpec.add(new SearchCriteria("batchId", batchId, SearchOperation.EQUAL));
		} else {
			parentSpec.add(new SearchCriteria("batchId", 0, SearchOperation.GREATER_THAN));
		}

		GenericSpecification<StudyMaterial> spec = new GenericSpecification<>();
		if (StringUtils.hasText(heading)) {
			spec.add(new SearchCriteria("heading", heading, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(subject)) {
			spec.add(new SearchCriteria("subject", subject, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<StudyMaterial> resultPage = null;
		if (pageSize > -1) {
			Page<StudyMaterial> page = repository.findAll(parentSpec.and(spec),pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<StudyMaterial> studyMaterialList = repository.findAll(parentSpec.and(spec),pageable.getSort());
			resultPage = genericConversion.convertToResultData(studyMaterialList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<StudyMaterial>> getAllStudyMaterial() {
		logger.info("inside getAllStudyMaterial...");
		List<StudyMaterial> studyMaterialsList = new ArrayList<>();
		try {
			studyMaterialsList = (List<StudyMaterial>) repository.findAll();
			if (studyMaterialsList.isEmpty()) {
				logger.info("findAll(null): studyMaterial");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(studyMaterialsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(studyMaterialsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudyMaterial> createStudyMaterial(StudyMaterial studyMaterial) {
		logger.info("inside createStudyMaterial...");
		try {
			long id = getStudyMaterialCount();
			studyMaterial.setId(++id);
			studyMaterial.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			studyMaterial.setDateofEntry(Util.getDateOfEntry(timestamp));
			studyMaterial.setCreatedBy(Util.getDefaultUser());
			studyMaterial.setCreatedAt(timestamp);
			studyMaterial.setUpdatedBy(Util.getDefaultUser());
			studyMaterial.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(studyMaterial), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createStudyMaterial - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<StudyMaterial> updateStudyMaterial(StudyMaterial studyMaterial) {
		logger.info("inside updateStudyMaterial...");
		try {
			Optional<StudyMaterial> optionalStudyMaterial = repository.findById(studyMaterial.getId());
			if (!optionalStudyMaterial.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			StudyMaterial existingStudyMaterial = optionalStudyMaterial.get();
			studyMaterial.setCreatedBy(existingStudyMaterial.getCreatedBy());
			studyMaterial.setCreatedAt(existingStudyMaterial.getCreatedAt());
			studyMaterial.setUpdatedBy(Util.getDefaultUser());
			studyMaterial.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(studyMaterial), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update studyMaterials for " + studyMaterial.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId...");
		return repository.count() + 1;
	}
}