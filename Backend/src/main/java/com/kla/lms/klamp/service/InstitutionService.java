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

import com.kla.lms.klamp.entity.Institution;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.InstitutionsRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class InstitutionService {
	public static final Logger logger = LoggerFactory.getLogger(InstitutionService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;

	@Autowired
	InstitutionsRepo repository;

	public long getInstitutionsCount() {
		logger.info("inside getInstitutionsCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Institution>> getInstitutionBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String institutionName, String principalName, Integer status) {
		logger.info("inside getDataBySearchCriteria..............");
		GenericConversion<Institution> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		GenericSpecification<Institution> genericSpecification = new GenericSpecification<>();
		if (StringUtils.hasText(institutionName)) {
			genericSpecification.add(new SearchCriteria("institutionName", institutionName, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(principalName)) {
			genericSpecification.add(new SearchCriteria("principalName", principalName, SearchOperation.EQUAL));
		}

		if (status != null && status>-1) {
			genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Institution> resultPage = null;
		if (pageSize > -1) {
			Page<Institution> page = repository.findAll(genericSpecification, pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Institution> institutionList = repository.findAll(genericSpecification, pageable.getSort());
			resultPage = genericConversion.convertToResultData(institutionList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Institution>> getAllInstitutions() {
		logger.info("inside getAllInstitutions...");
		List<Institution> institutionsList = new ArrayList<>();
		try {
			institutionsList = (List<Institution>) repository.findAll();
			if (institutionsList.isEmpty()) {
				logger.info("findAll(null): institution");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(institutionsList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(institutionsList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Institution> createInstitutions(Institution institution) {
		logger.info("inside createInstitutions...");
		try {
			long id = getInstitutionsCount();
			institution.setId(++id);
			institution.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			institution.setDateofEntry(Util.getDateOfEntry(timestamp));
			institution.setCreatedBy(Util.getDefaultUser());
			institution.setCreatedAt(timestamp);
			institution.setUpdatedBy(Util.getDefaultUser());
			institution.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(institution), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createFee - unsuccessfull: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Institution> updateInstitutions(Institution institution) {
		logger.info("inside updateInstitutions...");
		try {
			Optional<Institution> optionalInstitution = repository.findById(institution.getId());
			if (optionalInstitution.isPresent()) {
				Institution existingInstitution = optionalInstitution.get();
				existingInstitution.setStatus(PASSIVE_STATUS);
				existingInstitution.setUpdatedBy(Util.getDefaultUser());
				existingInstitution.setUpdatedAt(Util.getTimeStamp());
				repository.save(existingInstitution);
			}
			institution.setId(0);
			institution.setUpdatedAt(null);
			institution.setUpdatedAt(null);
			return createInstitutions(institution);

		} catch (Exception e) {
			logger.error("update Institution for " + institution.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId...");
		return repository.count() + 1;
	}
}