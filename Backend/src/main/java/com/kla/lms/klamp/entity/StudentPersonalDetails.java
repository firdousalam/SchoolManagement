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
@Table(name = "studentPersonalDetails", schema = "klamp", indexes = {
		@Index(name = "spd_idx_1", columnList = "id", unique = true),
		@Index(name = "spd_idx_2", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class StudentPersonalDetails extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "applicantName", nullable = false)
	private String applicantName;
	
	@Column(name = "fatherOrGuardian", nullable = false)
	private String fatherOrGuardian;
	
	@Column(name = "nameOfFatherorGuardian", nullable = false)
	private String nameOfFatherorGuardian;
	
	@Column(name = "dob", nullable = false)
	private String dob;
	
	@Column(name = "gender", nullable = false)
	private String gender;
	
	@Column(name = "nationality", nullable = false)
	private String nationality;
	
	@Column(name = "scOrSt", nullable = false)
	private String scOrSt;//clarification needed
	
	@Column(name = "isEmployed", nullable = false)
	private String isEmployed;//clarification needed
	
	@Column(name = "status", nullable = false)
	private int status;//to make it active and passive
	
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