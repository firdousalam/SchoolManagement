package com.kla.lms.klamp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "alerts", schema = "klamp", indexes = {
		@Index(name = "alt_idx_1", columnList = "id", unique = true),
		@Index(name = "alt_idx_2", columnList = "eventName", unique = false),
		@Index(name = "alt_idx_3", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Alerts extends AuditEntity{
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
}