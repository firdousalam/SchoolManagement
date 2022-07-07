package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Batch;

@Repository
public interface BatchRepo extends CrudRepository<Batch, Long>,JpaSpecificationExecutor<Batch> {

	@Query(nativeQuery = true, value = "Select * from klamp.batch where status =:status")
	public List<Batch> findBatchByStatus(@Param("status") String status);
}