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
import com.kla.lms.klamp.entity.Fees;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericJoinSpecification;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.FeesRepo;
import com.kla.lms.klamp.util.Util;

@Service
public class FeesService {
	public static final Logger logger = LoggerFactory.getLogger(FeesService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;

	@Autowired
	FeesRepo repository;

	@Autowired
	BatchService batchService;
	
	public long getFeesCount() {
		logger.info("inside getFeesCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<Fees>> getFeesBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy,  String category, long batchId, Integer status) {
		logger.info("inside getFeesBySearchCriteria..............");
		GenericConversion<Fees> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		GenericJoinSpecification<Batch,Fees> parentSpec = new GenericJoinSpecification<>();
		parentSpec.setParentName("batch");
		if (batchId > 0) {
			parentSpec.add(new SearchCriteria("id", batchId, SearchOperation.EQUAL));
		} else {
			parentSpec.add(new SearchCriteria("id", 0, SearchOperation.GREATER_THAN));
		}

		GenericSpecification<Fees> spec = new GenericSpecification<>();
		if (StringUtils.hasText(category)) {
			spec.add(new SearchCriteria("category", category, SearchOperation.EQUAL));
		}
		
		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}

		ResultPage<Fees> resultPage = null;
		if (pageSize > -1) {
			Page<Fees> page = repository.findAll(parentSpec.and(spec),pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<Fees> feesList = repository.findAll(parentSpec.and(spec),pageable.getSort());
			resultPage = genericConversion.convertToResultData(feesList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<Fees>> getAllfees() {
		logger.info("inside getAllfees..............");
		List<Fees> feesList = new ArrayList<>();
		try {
			feesList = (List<Fees>) repository.findAll();
			if (feesList.isEmpty()) {
				logger.info("findAll(null): fees");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(feesList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(feesList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Fees> createFee(Fees fee) {
		logger.info("inside createFee..............");
		try {
			Batch batch = getBatch(fee.getBatchId());
			fee.setBatch(batch);
			long id = getFeesCount();
			fee.setId(++id);
			fee.setStatus(ACTIVE_STATUS);
			String timestamp = Util.getTimeStamp();
			fee.setDateofEntry(Util.getDateOfEntry(timestamp));
			fee.setCreatedBy(Util.getDefaultUser());
			fee.setCreatedAt(timestamp);
			fee.setUpdatedBy(Util.getDefaultUser());
			fee.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(fee), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createFee - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Fees> updateFee(Fees fee) {
		logger.info("inside updateFee..............");
		try {
			Optional<Fees> optionalFees = repository.findById(fee.getId());
			
			if (optionalFees.isPresent()) {
				Fees existingFees = optionalFees.get();
				existingFees.setStatus(PASSIVE_STATUS);
				existingFees.setUpdatedBy(Util.getDefaultUser());
				existingFees.setUpdatedAt(Util.getTimeStamp());
				repository.save(existingFees);
			}
			fee.setId(0);
			fee.setUpdatedAt(null);
			fee.setUpdatedAt(null);
			return createFee(fee);

		} catch (Exception e) {
			logger.error("update fee for " + fee.getId(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public long getReferenceId() {
		logger.info("inside getReferenceId..............");
		return repository.count() + 1;
	}
	
	private Batch getBatch(long batchId) {
		return batchService.getById(batchId);
	}
}