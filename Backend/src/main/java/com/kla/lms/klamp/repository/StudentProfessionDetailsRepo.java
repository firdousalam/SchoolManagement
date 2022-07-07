package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudentProfessionDetails;

@Repository
public interface StudentProfessionDetailsRepo extends CrudRepository<StudentProfessionDetails, Long>,JpaSpecificationExecutor<StudentProfessionDetails>{

	@Query(nativeQuery = true, value = "Select * from klamp.student_professional_details where status =:status")
	public List<StudentProfessionDetails> findStudentProfessionalDetailsByStatus(@Param("status") String status);

	@Query(nativeQuery = true, value = "Select * from klamp.student_professional_details where profile_Id =:profileId")
	public List<StudentProfessionDetails> findStudentProfessionDetailsByProfileId(@Param("profileId") long profileId);
}