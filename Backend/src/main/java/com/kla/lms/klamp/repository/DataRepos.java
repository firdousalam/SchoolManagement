package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Data;

@Repository
public interface DataRepos extends CrudRepository<Data, Long>,JpaSpecificationExecutor<Data> {

	@Query(nativeQuery = true, value = "Select * from klamp.data where status = :status")
	public List<Data> findDataByStatus(@Param("status") String status);
}