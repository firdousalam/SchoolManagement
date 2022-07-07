package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Alerts;

@Repository
public interface AlertRepo extends CrudRepository<Alerts, Long>,JpaSpecificationExecutor<Alerts>{

	@Query(nativeQuery = true, value = "Select * from klamp.alerts where status =:status")
	public List<Alerts> findAlertsByStatus(@Param("status") String status);
}