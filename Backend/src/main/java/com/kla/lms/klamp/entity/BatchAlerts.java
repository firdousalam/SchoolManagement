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
@Table(name = "batch_alerts", schema = "klamp", indexes = {
		@Index(name = "ba_idx_1", columnList = "id", unique = true),
		@Index(name = "ba_idx_2", columnList = "eventName", unique = false),
		@Index(name = "ba_idx_3", columnList = "batchId", unique = false),
		@Index(name = "ba_idx_4", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class BatchAlerts extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "eventName", nullable = false)
	private String eventName;
	
	@Column(name = "alert1", nullable = false)
	private String alert1;
	
	@Column(name = "alert2", nullable = false)
	private String alert2;
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Column(name = "batchId", nullable = false)
	private long batchId;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long batchEventId;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String batchName;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long parentEventId;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "batchEventId")
	private BatchEvents batchEvents;

	public long getBatchEventId() {
		if (batchEvents != null) {
			return batchEvents.getId();
		}
		return batchEventId;
	}

	public void setBatchEventId(long batchEventId) {
		this.batchEventId = batchEventId;
	}

	public String getBatchName() {
		if (batchEvents != null) {
			return batchEvents.getBatchName();
		}
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public long getParentEventId() {
		if (batchEvents != null) {
			return batchEvents.getParentEventId();
		}
		return parentEventId;
	}

	public void setParentEventId(long parentEventId) {
		this.parentEventId = parentEventId;
	}

}