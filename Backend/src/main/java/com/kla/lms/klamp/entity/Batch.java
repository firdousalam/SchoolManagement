package com.kla.lms.klamp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "batch", schema = "klamp", indexes = {
		@Index(name = "bat_idx_1", columnList = "id", unique = true),
		@Index(name = "bat_idx_2", columnList = "batchName", unique = false),
		@Index(name = "bat_idx_3", columnList = "batchNumber", unique = false),
		@Index(name = "bat_idx_4", columnList = "batchYear", unique = false),
		@Index(name = "bat_idx_5", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Batch extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "batchName", nullable = false)
	private String batchName;
	
	@Column(name = "batchNumber", nullable = false)
	private String batchNumber;
	
	@Column(name = "batchYear", nullable = false)
	private String batchYear;
	
	@Column(name = "startDate", nullable = false)
	private String startDate;
	
	@Column(name = "endDate", nullable = false)
	private String endDate;
	
	@Column(name = "status", nullable = false)
	private int status;
}