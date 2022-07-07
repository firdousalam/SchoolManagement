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
@Table(name = "application", schema = "klamp", indexes = {
		@Index(name = "app_idx_1", columnList = "id", unique = true),
		@Index(name = "app_idx_2", columnList = "applicationState", unique = false),
		@Index(name = "app_idx_3", columnList = "applicationCategory", unique = false),
		@Index(name = "app_idx_4", columnList = "batchName", unique = false),
		@Index(name = "app_idx_5", columnList = "enrollmentNumbner", unique = false),
		@Index(name = "app_idx_6", columnList = "courseCenter", unique = false),
		@Index(name = "app_idx_7", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Application extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "applicationState", nullable = false)
	private String applicationState;//Draft, Submit, Approve, Reject
	
	@Column(name = "applicationCategory", nullable = false)
	private String applicationCategory;
	
	@Column(name = "batchName", nullable = false)
	private String batchName;
	
	@Column(name = "enrollmentNumbner", nullable = false)
	private String enrollmentNumbner;
	
	@Column(name = "courseCenter", nullable = false)
	private String courseCenter;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long userId;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long batchId;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private StudentProfile studentProfile;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "batchId")
	private Batch batch;

	public long getUserId() {
		if(studentProfile != null) {
			return studentProfile.getId();
		}
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getBatchId() {
		if (batch != null) {
			return batch.getId();
		}
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
}