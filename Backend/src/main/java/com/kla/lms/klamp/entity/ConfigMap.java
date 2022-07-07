package com.kla.lms.klamp.entity;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "configMap", schema = "klamp", indexes = {
		@Index(name = "cm_idx_1", columnList = "valueType1", unique = false),
		@Index(name = "cm_idx_2", columnList = "valueType2", unique = false),
		@Index(name = "cm_idx_3", columnList = "value1", unique = false),
		@Index(name = "cm_idx_4", columnList = "value2", unique = false)})
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class ConfigMap extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ConfigMapId id = new ConfigMapId();
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("value1")
	private Data data1;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("value2")
	private Data data2;
	
	@Column(name = "value1", nullable = false)
	private String value1;
	
	@Column(name = "value2", nullable = false)
	private String value2;
	
	@Column(name = "valueType1", nullable = false)
	private String valueType1;
	
	@Column(name = "valueType2", nullable = false)
	private String valueType2;

	@Column(name = "status", nullable = false)
	private int status;//to make it active and passive
}