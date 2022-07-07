package com.kla.lms.klamp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "institutions", schema = "klamp", indexes = {
		@Index(name = "ins_idx_1", columnList = "id", unique = true),
		@Index(name = "ins_idx_2", columnList = "institutionName", unique = false),
		@Index(name = "ins_idx_3", columnList = "principalName", unique = false),
		@Index(name = "ins_idx_4", columnList = "contactPersonName", unique = false),
		@Index(name = "ins_idx_5", columnList = "contactPersonDesignation", unique = false),
		@Index(name = "ins_idx_6", columnList = "phone", unique = false),
		@Index(name = "ins_idx_7", columnList = "mail", unique = false),
		@Index(name = "ins_idx_8", columnList = "status", unique = false) })
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class Institution extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	@Column(name = "institutionName", nullable = false)
	private String institutionName;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "principalName", nullable = false)
	private String principalName;
	
	@Column(name = "contactPersonName", nullable = false)
	private String contactPersonName;
	
	@Column(name = "contactPersonDesignation", nullable = false)
	private String contactPersonDesignation;
	
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "mail", nullable = false)
	private String mail;
	
	@Column(name = "remarks", nullable = false)
	private String remarks;
	
	@Column(name = "status", nullable = false)
	private int status;
}