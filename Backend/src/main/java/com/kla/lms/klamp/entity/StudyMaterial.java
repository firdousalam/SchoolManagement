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
@Table(name = "studyMaterial", schema = "klamp", indexes = {
		@Index(name = "sm_idx_1", columnList = "id", unique = true),
		@Index(name = "sm_idx_2", columnList = "subject", unique = false),
		@Index(name = "sm_idx_3", columnList = "heading", unique = false),
		@Index(name = "sm_idx_4", columnList = "accessLevel", unique = false),
		@Index(name = "sm_idx_5", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class StudyMaterial extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "subject", nullable = false)
	private String subject;
	
	@Column(name = "heading", nullable = false)
	private String heading;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "remarks", nullable = false)
	private String remarks;
	
	@Column(name = "malLink", nullable = false)
	private String malLink;
	
	@Column(name = "engLink", nullable = false)
	private String engLink;
	
	@Column(name = "accessLevel", nullable = false)
	private String accessLevel;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long batchId;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String batchName;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "batchId")
	private Batch batch;

	public long getBatchId() {
		if (batch != null) {
			return batch.getId();
		}
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		if (batch != null) {
			return batch.getBatchName();
		}
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
}