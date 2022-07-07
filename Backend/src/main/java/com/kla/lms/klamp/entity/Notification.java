package com.kla.lms.klamp.entity;

import java.io.Serializable;

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
@Table(name = "notification", schema = "klamp", indexes = {
		@Index(name = "nn_idx_1", columnList = "id", unique = true),
		@Index(name = "nn_idx_2", columnList = "notificationType", unique = false),
		@Index(name = "nn_idx_3", columnList = "sendTo", unique = false),
		@Index(name = "nn_idx_4", columnList = "students", unique = false),
		@Index(name = "nn_idx_5", columnList = "institutions", unique = false),
		@Index(name = "nn_idx_6", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Notification extends AuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "notificationType", nullable = false)
	private String notificationType;

	@Column(name = "sendTo", nullable = false)
	private String sendTo;
	
	@Column(name = "students", nullable = false)
	private String student;
	
	@Column(name = "institutions", nullable = false)
	private String institutions;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "attachmentLink1", nullable = false)
	private String attachmentLink1;
	
	@Column(name = "attachmentLink2", nullable = false)
	private String attachmentLink2;
	
	@Column(name = "status", nullable = false)
	private int status;
	
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