package com.kla.lms.klamp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kla.lms.klamp.entity.ConfigMap;
import com.kla.lms.klamp.entity.ResultPage;
import com.kla.lms.klamp.service.ConfigMapService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/configMap")
public class ConfigMapController {

	@Autowired
	ConfigMapService configMapService;

	@ApiOperation(value = "This method pulls the configMaps with Search Criteria")
	@GetMapping("/getConfigMapBySearchCriteria") // Search by configMap name, principal Name, status
	public ResponseEntity<ResultPage<ConfigMap>> getConfigMapBySearchCriteria(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(name = "sortBy", required = false, defaultValue = "") String sortBy,
			@RequestParam(name = "paramType1", required = true) String paramType1,
			@RequestParam(name = "paramType2", required = true) String paramType2,
			@RequestParam(name = "value1", required = false) String value1,
			@RequestParam(name = "value2", required = false) String value2,
			@RequestParam(name = "status", required = false, defaultValue = "-1") Integer status) {
		return configMapService.getConfigMapsBySearchCriteria(pageNumber, pageSize, sortDirection, sortBy, paramType1,
				paramType2, value1, value2, status);
	}

	@ApiOperation(value = "This method pulls All the configMaps")
	@GetMapping("getAllConfigMap")
	public ResponseEntity<List<ConfigMap>> getAllConfigMap() {
		return configMapService.getAllConfigMaps();
	}

	@ApiOperation(value = "This method Creates New ConfigMap")
	@PostMapping("/createConfigMap")
	public ResponseEntity<ConfigMap> createConfigMap(@RequestParam(name = "id1", required = true) long id1, 
			@RequestParam(name = "id2", required = true) long id2) {
		return configMapService.createConfigMaps(id1, id2);
	}
	
	@ApiOperation(value = "This method saves ConfigMap for list provided ")
	@PostMapping("/saveConfigMaps")
	public ResponseEntity<List<ConfigMap>> saveConfigMaps(@RequestParam(name = "id1", required = true) long id1, 
			@RequestBody List<Long> id2List) {
		return configMapService.saveConfigMaps(id1, id2List);
	}

	@ApiOperation(value = "This method Updates the ConfigMap to Passive")
	@PutMapping("/updateConfigMap")
	public ResponseEntity<ConfigMap> updateConfigMap(@RequestParam(name = "id1", required = true) long id1, 
			@RequestParam(name = "id2", required = true) long id2) {
		return configMapService.updateConfigMaps(id1, id2);
	}

	@ApiOperation(value = "This method returns ConfigMap by id")
	@GetMapping("/getConfigMapById")
	public ResponseEntity<ConfigMap> getConfigMapById(@RequestParam(name = "id1", required = true) long id1, 
			@RequestParam(name = "id2", required = true) long id2) {
		return configMapService.getConfigMapsById(id1, id2);
	}

	@ApiOperation(value = "This method hard deletes ConfigMap by id")
	@DeleteMapping("/deleteConfigMapById")
	public ResponseEntity<ConfigMap> deleteConfigMapById(@RequestParam(name = "id1", required = true) long id1, 
			@RequestParam(name = "id2", required = true) long id2) {
		return configMapService.deleteConfigMapsById(id1, id2);
	}
}