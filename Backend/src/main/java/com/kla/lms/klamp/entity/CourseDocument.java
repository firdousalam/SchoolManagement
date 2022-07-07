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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "courseDocument", schema = "klamp", indexes = {
		@Index(name = "cd_idx_1", columnList = "id", unique = true),
		@Index(name = "cd_idx_2", columnList = "documentType", unique = false),
		@Index(name = "cd_idx_3", columnList = "heading", unique = false),
		@Index(name = "cd_idx_4", columnList = "topic", unique = false),
		@Index(name = "cd_idx_5", columnList = "startDate", unique = false),
		@Index(name = "cd_idx_6", columnList = "endDate", unique = false),
		@Index(name = "cd_idx_7", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class CourseDocument extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "documentType", nullable = false)
	private String documentType;
	
	@Column(name = "heading", nullable = false)
	private String heading;
	
	@Column(name = "topic", nullable = false)
	private String topic;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "remark", nullable = false)
	private String remark;
	
	@Column(name = "startDate", nullable = false)
	private String startDate;
	
	@Column(name = "endDate", nullable = false)
	private String endDate;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Column(name = "attachmentlink1", nullable = false)
	private String attachmentlink1;
	
	@Column(name = "attachmentlink2", nullable = false)
	private String attachmentlink2;

	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long batchId;
	
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
	
}