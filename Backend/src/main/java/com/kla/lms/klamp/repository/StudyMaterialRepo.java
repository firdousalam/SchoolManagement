package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.StudyMaterial;

@Repository
public interface StudyMaterialRepo extends CrudRepository<StudyMaterial, Long>,JpaSpecificationExecutor<StudyMaterial> {

	@Query(nativeQuery = true, value = "Select * from klamp.studyMaterial where status =:status")
	public List<StudyMaterial> findStudyMaterialByStatus(@Param("status") String status);
}