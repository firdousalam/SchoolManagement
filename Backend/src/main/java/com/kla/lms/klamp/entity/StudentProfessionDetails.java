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
@Table(name = "studentProfessionalDetails", schema = "klamp", indexes = {
		@Index(name = "sprofd_idx_1", columnList = "id", unique = true),
		@Index(name = "sprofd_idx_2", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class StudentProfessionDetails extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY

	@Column(name = "organization", nullable = false)
	private String organization;

	@Column(name = "designation", nullable = false)
	private String designation;

	@Column(name = "placeOfDuty", nullable = false)
	private String placeOfDuty;

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
		if(studentProfile != null) {
			studentProfile.getUserId();
		}
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}