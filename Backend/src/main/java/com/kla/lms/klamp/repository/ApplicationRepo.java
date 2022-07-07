package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Application;

@Repository
public interface ApplicationRepo extends CrudRepository<Application, Long>,JpaSpecificationExecutor<Application> {

	@Query(nativeQuery = true, value = "Select * from klamp.application where status =:status")
	public List<Application> findAlertsByStatus(@Param("status") String status);
	
	@Query(nativeQuery = true, value = "Select * from klamp.application where profile_Id =:profileId")
	public List<Application> findApplicationByProfileId(@Param("profileId") long profileId);
}