package com.kla.lms.klamp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
@MappedSuperclass
@lombok.Data
public class AuditEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "createdAt", nullable = false, updatable = false)
	private String createdAt;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "updatedAt")
	private String updatedAt;

	@Column(name = "updatedBy")
	private String updatedBy;
	
}