package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Institution;

@Repository
public interface InstitutionsRepo extends CrudRepository<Institution, Long>,JpaSpecificationExecutor<Institution> {

	@Query(nativeQuery = true, value = "Select * from klamp.institutions where status =:status")
	public List<Institution> findInstitutionsByStatus(@Param("status") String status);	
}