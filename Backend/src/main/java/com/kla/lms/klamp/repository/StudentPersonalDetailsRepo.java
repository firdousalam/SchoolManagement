package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudentPersonalDetails;

@Repository
public interface StudentPersonalDetailsRepo extends CrudRepository<StudentPersonalDetails, Long>,JpaSpecificationExecutor<StudentPersonalDetails> {

	@Query(nativeQuery = true, value = "Select * from klamp.student_personal_details where status =:status")
	public List<StudentPersonalDetails> findStudentPersonalDeatilsByStatus(@Param("status") String status);

	@Query(nativeQuery = true, value = "Select * from klamp.student_personal_details where profile_Id =:profileId")
	public StudentPersonalDetails findStudentPersonalDetailsByProfileId(@Param("profileId") long profileId);

}