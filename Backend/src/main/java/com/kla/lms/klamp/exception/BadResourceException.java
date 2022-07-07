package com.kla.lms.klamp.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BadResourceException extends Exception {
 
	private static final long serialVersionUID = 1L;
	private final List<String> errorMessages = new ArrayList<>();
            
 
    public BadResourceException(String msg) {
        super(msg);
    }
    
    public void addErrorMessage(String message) {
        this.errorMessages.add(message);
    }
}