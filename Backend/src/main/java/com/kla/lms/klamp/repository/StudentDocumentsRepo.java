package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudentDocuments;

@Repository
public interface StudentDocumentsRepo extends CrudRepository<StudentDocuments, Long>,JpaSpecificationExecutor<StudentDocuments> {

	@Query(nativeQuery = true, value = "Select * from klamp.documents where status =:status")
	public List<StudentDocuments> findStudentDocumentsByStatus(@Param("status") String status);
	
	@Query(nativeQuery = true, value = "Select * from klamp.documents where profile_Id =:profileId")
	public List<StudentDocuments> findStudentDocumentsByProfileId(@Param("profileId") long profileId);
}