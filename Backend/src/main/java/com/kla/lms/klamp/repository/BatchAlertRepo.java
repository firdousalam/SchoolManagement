package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.BatchAlerts;

@Repository
public interface BatchAlertRepo extends CrudRepository<BatchAlerts, Long>,JpaSpecificationExecutor<BatchAlerts> {

	@Query(nativeQuery = true, value = "Select * from klamp.batch_alerts where status =:status")
	public List<BatchAlerts> findBatchAlertsByStatus(@Param("status") String status);
}