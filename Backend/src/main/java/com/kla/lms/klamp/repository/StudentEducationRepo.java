package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudentEducations;

@Repository
public interface StudentEducationRepo extends CrudRepository<StudentEducations, Long>,JpaSpecificationExecutor<StudentEducations> {

	@Query(nativeQuery = true, value = "Select * from klamp.student_education_details where status =:status")
	public List<StudentEducations> findStudentEducationDetailsByStatus(@Param("status") String status);
	
	@Query(nativeQuery = true, value = "Select * from klamp.student_education_details where profile_Id =:profileId")
	public List<StudentEducations> findStudentEducationsByProfileId(@Param("profileId") long profileId);
}