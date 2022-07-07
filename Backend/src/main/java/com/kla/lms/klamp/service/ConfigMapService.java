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

import com.kla.lms.klamp.entity.ConfigMap;
import com.kla.lms.klamp.entity.ConfigMapId;
import com.kla.lms.klamp.entity.Data;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.exception.AppException;
import com.kla.lms.klamp.generic.GenericConversion;
import com.kla.lms.klamp.generic.GenericSpecification;
import com.kla.lms.klamp.generic.SearchCriteria;
import com.kla.lms.klamp.generic.SearchOperation;
import com.kla.lms.klamp.repository.ConfigMapRepos;
import com.kla.lms.klamp.util.Util;

@Service
public class ConfigMapService {
	public static final Logger logger = LoggerFactory.getLogger(ConfigMapService.class);
	private static final int ACTIVE_STATUS = 1;
	private static final int PASSIVE_STATUS = 0;

	@Autowired
	ConfigMapRepos repository;

	@Autowired
	DataService dataService;

	public long getConfigMapCount() {
		logger.info("inside getConfigMapCount..............");
		return repository.count();
	}

	public ResponseEntity<ResultPage<ConfigMap>> getConfigMapsBySearchCriteria(Integer pageNumber, Integer pageSize,
			String sortDirection, String sortBy, String paramType1, String paramType2, String value1, String value2, Integer status) {
		logger.info("inside getConfigMapsBySearchCriteria..............");
		GenericConversion<ConfigMap> genericConversion = new GenericConversion<>();
		Pageable pageable = genericConversion.getPageable(pageNumber, pageSize, sortDirection, sortBy);
		
		GenericSpecification<ConfigMap> spec = new GenericSpecification<>();

		if (StringUtils.hasText(paramType1)) {
			spec.add(new SearchCriteria("valueType1", paramType1, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(paramType2)) {
			spec.add(new SearchCriteria("valueType2", paramType2, SearchOperation.EQUAL));
		}

		if (StringUtils.hasText(value1)) {
			spec.add(new SearchCriteria("value1", value1, SearchOperation.EQUAL));
		}
		
		if (StringUtils.hasText(value2)) {
			spec.add(new SearchCriteria("value2", value2, SearchOperation.EQUAL));
		}

		if (status != null && status > -1) {
			spec.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
		}
		
		ResultPage<ConfigMap> resultPage = null;
		if (pageSize > -1) {
			Page<ConfigMap> page = repository.findAll(spec, pageable);
			resultPage = genericConversion.convertToResultData(page);
		} else {
			List<ConfigMap> configMapList = repository.findAll(spec, pageable.getSort());
			resultPage = genericConversion.convertToResultData(configMapList);
		}
		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	public ResponseEntity<List<ConfigMap>> getAllConfigMaps() {
		logger.info("inside getAllConfigMaps..............");
		List<ConfigMap> valueList = new ArrayList<>();
		try {
			valueList = (List<ConfigMap>) repository.findAll();
			if (valueList.isEmpty()) {
				logger.info("findAll(null): configMap");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(valueList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("findAll fetching error ", e);
			return new ResponseEntity<>(valueList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<List<ConfigMap>> saveConfigMaps(long id1, List<Long> id2List) {
		logger.info("inside saveConfigMaps..............");
		try {
			List<ConfigMap> configMapList = new ArrayList<>();
			repository.deleteByData1Id(id1);
			for (long id2: id2List) {
				ConfigMap configMap = new ConfigMap();
				Data dataValue1 = getDataValue(id1);
				Data dataValue2 = getDataValue(id2);
				configMap.setData1(dataValue1);
				configMap.setData2(dataValue2);
				configMap.setValue1(dataValue1.getName());
				configMap.setValue2(dataValue2.getName());
				configMap.setValueType1(dataValue1.getReferenceType());
				configMap.setValueType2(dataValue2.getReferenceType());
				ConfigMapId configMapId = new ConfigMapId();
				configMapId.setValue1(id1);
				configMapId.setValue2(id2);
				configMap.setId(configMapId);
				configMap.setStatus(ACTIVE_STATUS);
				configMap.setCreatedBy(Util.getDefaultUser());
				configMap.setCreatedAt(Util.getTimeStamp());
				configMap.setUpdatedBy(Util.getDefaultUser());
				configMap.setUpdatedAt(Util.getDefaultTimestamp());
				ConfigMap savedConfigMap = repository.save(configMap);
				configMapList.add(savedConfigMap);
			}
			return new ResponseEntity<>(configMapList, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("saveConfigMaps - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<ConfigMap> createConfigMaps(long id1, long id2) {
		logger.info("inside createConfigMaps..............");
		try {
			Optional<ConfigMap> optionalExistingConfigMaps = repository.findByData1IdAndData2Id(id1, id2);
			if (optionalExistingConfigMaps.isPresent()) {
				repository.deleteByData1IdAndData2Id(id1, id2);
			}
			
			ConfigMap configMap = new ConfigMap();
			Data dataValue1 = getDataValue(id1);
			Data dataValue2 = getDataValue(id2);
			configMap.setData1(dataValue1);
			configMap.setData2(dataValue2);
			configMap.setValue1(dataValue1.getName());
			configMap.setValue2(dataValue2.getName());
			configMap.setValueType1(dataValue1.getReferenceType());
			configMap.setValueType2(dataValue2.getReferenceType());
			ConfigMapId configMapId = new ConfigMapId();
			configMapId.setValue1(id1);
			configMapId.setValue2(id2);
			configMap.setId(configMapId);
			configMap.setStatus(ACTIVE_STATUS);
			configMap.setCreatedBy(Util.getDefaultUser());
			configMap.setCreatedAt(Util.getTimeStamp());
			configMap.setUpdatedBy(Util.getDefaultUser());
			configMap.setUpdatedAt(Util.getDefaultTimestamp());
			return new ResponseEntity<>(repository.save(configMap), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("createConfigMaps - unsuccessful: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<ConfigMap> updateConfigMaps(long id1, long id2) {
		logger.info("inside updateConfigMaps..............");
		try {
			ConfigMapId configMapId = new ConfigMapId();
			configMapId.setValue1(id1);
			configMapId.setValue2(id2);
			Optional<ConfigMap> optionalExistingConfigMaps = repository.findByData1IdAndData2Id(id1, id2);
			
			if (!optionalExistingConfigMaps.isPresent()) {
				throw new AppException("ConfigMap not found for - id1: " + id1 + " and id2: " + id2);
			}
			
			ConfigMap existingConfigMaps = optionalExistingConfigMaps.get();
			existingConfigMaps.setStatus(PASSIVE_STATUS);
			existingConfigMaps.setUpdatedBy(Util.getDefaultUser());
			existingConfigMaps.setUpdatedAt(Util.getTimeStamp());
			return new ResponseEntity<>(repository.save(existingConfigMaps), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update configMaps for " + id1 + ":" + id2, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<ConfigMap> getConfigMapsById(long id1, long id2) {
		logger.info("inside updateConfigMaps..............");
		try {
			Optional<ConfigMap> optionalExistingConfigMaps = repository.findByData1IdAndData2Id(id1, id2);
			
			if (!optionalExistingConfigMaps.isPresent()) {
				return new ResponseEntity<>(new ConfigMap(), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(optionalExistingConfigMaps.get(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get configMaps for " + id1 + ":" + id2, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<ConfigMap> deleteConfigMapsById(long id1, long id2) {
		logger.info("inside deleteConfigMapsById..............");
		ConfigMap configMap = new ConfigMap();
		try {
			Optional<ConfigMap> optionalExistingConfigMaps = repository.findByData1IdAndData2Id(id1, id2);
			if (optionalExistingConfigMaps.isPresent()) {
				configMap = optionalExistingConfigMaps.get();
				repository.deleteByData1IdAndData2Id(id1, id2);
			}
			return new ResponseEntity<>(configMap, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("delete configMaps for " + id1 + ":" + id2, e);
			return new ResponseEntity<>(configMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Data getDataValue(long id) {
		return dataService.getById(id);
	}
}