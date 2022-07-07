package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Fees;

@Repository
public interface FeesRepo extends CrudRepository<Fees, Long>,JpaSpecificationExecutor<Fees> {

	@Query(nativeQuery = true, value = "Select * from klamp.fees where status =:status")
	public List<Fees> findFeesByStatus(@Param("status") String status);
}