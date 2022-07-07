package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudentProfile;

@Repository
public interface StudentProfileRepo extends CrudRepository<StudentProfile, Long>,JpaSpecificationExecutor<StudentProfile>{

	@Query(nativeQuery = true, value = "Select * from klamp.studentProfile where status =:status")
	public List<StudentProfile> findStudentProfileByStatus(@Param("status") String status);
	
	@Query(nativeQuery = true, value = "Select * from klamp.studentProfile where userId = :userId")
	public StudentProfile findProfileByUserId(@Param("userId") long userId);

}