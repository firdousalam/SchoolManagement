package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudentContactDetails;

@Repository
public interface StudentContactDetailsRepo extends CrudRepository<StudentContactDetails, Long>,JpaSpecificationExecutor<StudentContactDetails>{

	@Query(nativeQuery = true, value = "Select * from klamp.contactDetails where status =:status")
	public List<StudentContactDetails> findAlertsStudentContactDetailsByStatus(@Param("status") String status);
	
	@Query(nativeQuery = true, value = "Select * from klamp.contact_details where profile_Id =:profileId")
	public List<StudentContactDetails> findStudentContactDetailsByProfileId(@Param("profileId") long profileId);
}