package com.kla.lms.klamp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name = "studentProfile", schema = "klamp", indexes = {
		@Index(name = "sp_idx_1", columnList = "id", unique = true),
		@Index(name = "sp_idx_2", columnList = "id", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class StudentProfile extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "userId", nullable = false)
	private long userId;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Transient
	private StudentPersonalDetails studentPersonalDetails;
	
	@Transient
	private List<StudentContactDetails> studentContactDetailsList = new ArrayList<>();
	
	@Transient
	private List<StudentEducations> studentEducationDetailsList = new ArrayList<>();
	
	@Transient
	private List<StudentProfessionDetails> studentProfessionalDetailsList = new ArrayList<>();
	
	@Transient
	private List<StudentDocuments> studentDocumentsList = new ArrayList<>();
	
	@Transient
	private List<Application> applicationList = new ArrayList<>();
}