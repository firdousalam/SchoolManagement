package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Type;

@Repository
public interface TypeRepos extends CrudRepository<Type, Long>,JpaSpecificationExecutor<Type> {

	@Query(nativeQuery = true, value = "Select * from klamp.type where status = :status")
	public List<Type> findTypesByStatus(@Param("status") String status);
	
	@Query(nativeQuery = true, value = "Select * from klamp.type where reference_type = :referenceType")
	public Type findByReferenceType(@Param("referenceType") String referenceType);
}