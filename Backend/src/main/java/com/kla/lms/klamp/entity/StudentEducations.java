package com.kla.lms.klamp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "studentEducationDetails", schema = "klamp", indexes = {
		@Index(name = "sed_idx_1", columnList = "id", unique = true),
		@Index(name = "sed_idx_2", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class StudentEducations extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY

	@Column(name = "sequence")
	private int sequence;

	@Column(name = "courseName", nullable = false)
	private String courseName;

	@Column(name = "institution", nullable = false)
	private String institution;

	@Column(name = "educationStatus", nullable = false)
	private String educationStatus;

	@Column(name = "yearofCompletion")
	private String yearofCompletion;

	@Column(name = "percentage")
	private String percentage;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long userId;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private StudentProfile studentProfile;

	public long getUserId() {
		if (studentProfile != null) {
			return studentProfile.getUserId();
		}
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}