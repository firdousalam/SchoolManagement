package com.kla.lms.klamp.exception;

import java.io.Serializable;

public class ErrorResponse  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public ErrorResponse(Integer messageCode, String message) {
		super();
		this.messageCode = messageCode;
		this.message = message;
	}
	
	public ErrorResponse(Integer messageCode, String message,String displayMessage) {
		super();
		this.messageCode = messageCode;
		this.message = message;
		this.displayMessage = displayMessage;
	}
	
	private int messageCode;
	private String message;
	private String displayMessage;
	
	public int getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDisplayMessage() {
		return displayMessage;
	}
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
}