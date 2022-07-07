package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.CourseDocument;

@Repository
public interface CourseDocumentRepo extends CrudRepository<CourseDocument, Long>,JpaSpecificationExecutor<CourseDocument>{

	@Query(nativeQuery = true, value = "Select * from klamp.courseDocument where status =:status")
	public List<CourseDocument> findCourseDocumentByStatus(@Param("status") String status);
}