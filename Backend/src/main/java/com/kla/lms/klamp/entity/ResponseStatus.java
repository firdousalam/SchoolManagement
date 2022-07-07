package com.kla.lms.klamp.entity;

@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class ResponseStatus{
	private int messageCode;
	private String message;
	private String displayMessage;
}