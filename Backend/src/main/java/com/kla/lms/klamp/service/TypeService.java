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
import com.kla.lms.klamp.entity.Type;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.TypeRepos;
import com.kla.lms.klamp.util.Util;

@Service
public class TypeService {
	public static final Logger logger = LoggerFactory.getLogger(TypeService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	TypeRepos repository;
	
	public long getTypeCount() {
		logger.info("inside getTypeCount..............");
		return repository.count();
	}
	public Type getType(String referenceType) {
		return repository.findByReferenceType(referenceType);
	}

	public ResponseEntity<ResultPage<Type>> getTypeBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String referenceType, Integer status) {
		logger.info("inside getTypeBySearchCriteria..............");
		GenericConversion<Type> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		GenericSpecification<Type> genericSpecification = new GenericSpecification<>();
		if (StringUtils.hasText(referenceType)) {
			genericSpecification.add(new SearchCriteria("referenceType", referenceType, SearchOperation.EQUAL));
		}
		
		if (status != null && status>-1) {
			genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Type> resultPage = null;
		if (pageSize > -1) {
			Page<Type> page = repository.findAll(genericSpecification, pageable); 
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Type> typeList = repository.findAll(genericSpecification, pageable.getSort());
			resultPage = genericConversion.convertToResultData(typeList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Type>> getAllType() {
		logger.info("inside getAllType..............");
		List<Type> typeList = new ArrayList<>();
		try {
			typeList = (List<Type>) repository.findAll();
			if (typeList.isEmpty()) {
				logger.info("findAll(null): type");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(typeList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(typeList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Type> createType(Type type) {
		logger.info("inside createType..............");
		try {
			long id = getTypeCount();
			type.setId(++id);
			type.setStatus(ACTIVE_STATUS);
			type.setCreatedBy(Util.getDefaultUser());// post user entity it will be updated
			type.setCreatedAt(Util.getTimeStamp());
			type.setUpdatedBy(Util.getDefaultUser());
			type.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(type), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createType - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Type> updateType(Type type) {
		logger.info("inside updateType..............");
		try {
			Optional<Type> optionalType =  repository.findById(type.getId());
			if (!optionalType.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Type existingType = optionalType.get();
			type.setCreatedAt(existingType.getCreatedAt());
			type.setCreatedBy(existingType.getCreatedBy());
			type.setUpdatedBy(Util.getDefaultUser());
			type.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(type), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update type for " + type.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
}