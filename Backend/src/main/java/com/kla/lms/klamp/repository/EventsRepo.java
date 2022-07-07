package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Events;

@Repository
public interface EventsRepo extends CrudRepository<Events, Long>,JpaSpecificationExecutor<Events>{

	@Query(nativeQuery = true, value = "Select * from klamp.events where status =:status")
	public List<Events> findEventsByStatus(@Param("status") String status);
}