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
import com.kla.lms.klamp.exception.AppException;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.BatchRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class BatchService {
	public static final Logger logger = LoggerFactory.getLogger(BatchService.class);
	private static final int ACTIVE_STATUS = 1;
	
	@Autowired
	BatchRepo batchRepo;

	public long getBatchCount() {
		logger.info("inside getBatchCount...");
		return batchRepo.count();
	}

	public ResponseEntity<ResultPage<Batch>> getBatchBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String batchName, String batchNumber, Integer status) {
		logger.info("inside getBatchBySearchCriteria..............");
		GenericSpecification<Batch > genericSpecification = new GenericSpecification<>();
		GenericConversion<Batch> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		if (StringUtils.hasText(batchName)) {
			genericSpecification.add(new SearchCriteria("batchName", batchName, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(batchNumber)) {
			genericSpecification.add(new SearchCriteria("batchNumber", batchNumber, SearchOperation.EQUAL));
		}
		
		if (status != null && status>-1) {
			genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<Batch> resultPage = null;
		if (pageSize > -1) {
			Page<Batch> page = batchRepo.findAll(genericSpecification, pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Batch> batchList = batchRepo.findAll(genericSpecification, pageable.getSort());
			resultPage = genericConversion.convertToResultData(batchList);
		}
		
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Batch>> getAllBatch() {
		logger.info("inside getAllBatch...");
		List<Batch> batchList = new ArrayList<>();
		try {
			batchList = (List<Batch>) batchRepo.findAll();
			if (batchList.isEmpty()) {
				logger.info("findAll(null): batch");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(batchList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(batchList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Batch> createBatch(Batch batch) {
		logger.info("inside createBatch...");
		try {
			long id = getBatchCount();
			batch.setId(++id);
			batch.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			batch.setDateofEntry(Util.getDateOfEntry(timestamp));
			batch.setCreatedBy(Util.getDefaultUser());
			batch.setCreatedAt(timestamp);
			batch.setUpdatedBy(Util.getDefaultUser());
			batch.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(batchRepo.save(batch), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createBatch - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Batch> updateBatch(Batch batch) {
		logger.info("inside updateBatch...");
		try {
			Optional<Batch> optionalBatch = batchRepo.findById(batch.getId());
			if (!optionalBatch.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Batch existingBatch = optionalBatch.get();
			batch.setCreatedAt(existingBatch.getCreatedAt());
			batch.setCreatedBy(existingBatch.getCreatedBy());
			batch.setUpdatedBy(Util.getDefaultUser());
			batch.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(batchRepo.save(batch), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update batch for " + batch.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId...");
		return batchRepo.count() + 1;
	}
	
	public Batch getById(long id) {
		return batchRepo.findById(id).orElseThrow(() -> new AppException("Batch not found for id: "+ id));
	}
}