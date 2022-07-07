package com.kla.lms.klamp.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class ConfigMapId implements Serializable{
	private static final long serialVersionUID = 1L;

	private long value1;
	private long value2;
}