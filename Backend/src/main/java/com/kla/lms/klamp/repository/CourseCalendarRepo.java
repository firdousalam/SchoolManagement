package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.CourseCalendar;

@Repository
public interface CourseCalendarRepo extends CrudRepository<CourseCalendar, Long>,JpaSpecificationExecutor<CourseCalendar> {

	@Query(nativeQuery = true, value = "Select * from klamp.courseCalendar where status =:status")
	public List<CourseCalendar> findCourseCalendarByStatus(@Param("status") String status);
}