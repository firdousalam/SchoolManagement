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
@Table(name = "fees", schema = "klamp", indexes = {
		@Index(name = "fees_idx_1", columnList = "id", unique = true),
		@Index(name = "fees_idx_2", columnList = "category", unique = false),
		@Index(name = "fees_idx_3", columnList = "applicationFees", unique = false),
		@Index(name = "fees_idx_4", columnList = "admissionFees", unique = false),
		@Index(name = "fees_idx_5", columnList = "tuitionFees", unique = false),
		@Index(name = "fees_idx_6", columnList = "startDate", unique = false),
		@Index(name = "fees_idx_7", columnList = "endDate", unique = false),
		@Index(name = "fees_idx_8", columnList = "fineTuitionFees", unique = false),
		@Index(name = "fees_idx_9", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Fees extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "category", nullable = false)
	private String category;
	
	@Column(name = "applicationFees", nullable = false)
	private String applicationFees;
	
	@Column(name = "admissionFees", nullable = false)
	private String admissionFees;
	
	@Column(name = "tuitionFees", nullable = false)
	private String tuitionFees;
	
	@Column(name = "startDate", nullable = false)
	private String startDate;
	
	@Column(name = "endDate", nullable = false)
	private String endDate;
	
	@Column(name = "fineTuitionFees", nullable = false)
	private String fineTuitionFees;
	
	@Column(name = "remarks", nullable = false)
	private String remarks;
	
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
		if(batch != null) {
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