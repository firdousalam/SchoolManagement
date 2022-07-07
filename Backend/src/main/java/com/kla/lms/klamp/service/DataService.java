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

import com.kla.lms.klamp.entity.Data;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.entity.Type;
import com.kla.lms.klamp.exception.AppException;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.DataRepos;
import com.kla.lms.klamp.util.Util;

@Service
public class DataService {
	public static final Logger logger = LoggerFactory.getLogger(DataService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	DataRepos repository;

	@Autowired
	TypeService typeService;
	
	public long getDataCount() {
		logger.info("inside getDataCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Data>> getDataBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String name, String referenceType, Integer status) {
		logger.info("inside getDataBySearchCriteria..............");
		GenericConversion<Data> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		GenericJoinSpecification<Type,Data> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("type");
		parentSpec.add(new SearchCriteria("referenceType", referenceType, SearchOperation.EQUAL));
		
		GenericSpecification<Data> spec = new GenericSpecification<>();
		if (StringUtils.hasText(name)) {
			spec.add(new SearchCriteria("name", name, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Data> resultPage = null;
		if (pageSize > -1) {
			Page<Data> page = repository.findAll(parentSpec.and(spec),pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Data> dataList = repository.findAll(parentSpec.and(spec),pageable.getSort());
			resultPage = genericConversion.convertToResultData(dataList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}
	
	public ResponseEntity<List<Data>> getAllData() {
		logger.info("inside getAllData..............");
		List<Data> dataList = new ArrayList<>();
		try {
			dataList = (List<Data>) repository.findAll();
			
			if (dataList.isEmpty()) {
				logger.info("findAll(null)");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(dataList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(dataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Data> createData(Data data) {
		logger.info("inside createData..............");
		try {
			Type type = typeService.getType(data.getReferenceType());
			long id = getDataCount();
			data.setType(type);
			data.setId(++id);
			data.setStatus(ACTIVE_STATUS);
			data.setCreatedBy(Util.getDefaultUser());
			data.setCreatedAt(Util.getTimeStamp());
			data.setUpdatedBy(Util.getDefaultUser());
			data.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(data), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createData - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Data> updateData(Data data) {
		logger.info("inside updateData..............");
		try {
			Optional<Data> optionalData = repository.findById(data.getId());
			if (!optionalData.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Data existingData = optionalData.get();
			data.setCreatedAt(existingData.getCreatedAt());
			data.setCreatedBy(existingData.getCreatedBy());
			data.setUpdatedBy(Util.getDefaultUser());
			data.setUpdatedAt(Util.getTimeStamp());
			return new ResponseEntity<>(repository.save(data), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update datas for " + data.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
	
	public Data getById(long id) {
		logger.info("inside getById..............");
		return repository.findById(id).orElseThrow(() -> new AppException("Data not found for id: " + id));
	}
}