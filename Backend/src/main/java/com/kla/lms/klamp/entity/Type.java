package com.kla.lms.klamp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "type", schema = "klamp", indexes = {
		@Index(name = "type_idx_1", columnList = "id", unique = true),
		@Index(name = "type_idx_2", columnList = "referenceType", unique = true),
		@Index(name = "type_idx_3", columnList = "status", unique = false)})
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Type extends AuditEntity{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "referenceType", nullable = false)
	private String referenceType;
		
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "status", nullable = false)
	private int status;
}