package com.kla.lms.klamp.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.ConfigMap;
import com.kla.lms.klamp.entity.ConfigMapId;

@Repository
public interface ConfigMapRepos extends CrudRepository<ConfigMap, ConfigMapId>,JpaSpecificationExecutor<ConfigMap>{

	@Query(nativeQuery = true, value = "Select * from klamp.configMap where status = :status")
	public List<ConfigMap> findConfigMapByStatus(@Param("status") String status);
	
	public Optional<ConfigMap> findByData1IdAndData2Id(long value1, long value2);
	
	@Transactional
	@Modifying
	public void deleteByData1IdAndData2Id(long value1, long value2);

	@Transactional
	@Modifying
	public void deleteByData1Id(long value1);
}
