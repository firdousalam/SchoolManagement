package com.kla.lms.klamp.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.BatchEvents;

@Repository
public interface BatchEventRepo extends CrudRepository<BatchEvents, Long>,JpaSpecificationExecutor<BatchEvents> {

	@Query(nativeQuery = true, value = "Select * from klamp.batch_events where status =:status")
	public List<BatchEvents> findBatchEventsByStatus(@Param("status") String status);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "Update klamp.batch_events set status = 0 where batch_id =:id")
	public void deactivateByBatchId(long id);
}