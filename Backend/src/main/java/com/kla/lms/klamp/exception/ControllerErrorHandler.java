package com.kla.lms.klamp.exception;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ControllerErrorHandler {
	public static final Logger logger = LoggerFactory.getLogger(ControllerErrorHandler.class);
	private static final String LOG_MESSAGE = "Error Message: {}, Cause: {}, Exception: {}";
	private static final String SERVER_ERROR_MESSAGE = "Server Error!";

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
				new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "File too large!", "File too large!"));
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		logger.error(LOG_MESSAGE, ex.getMessage(), ex.getCause());

		String errorFields = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getField)
				.collect(Collectors.joining(";"));

		String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining(";"));

		ErrorResponse errorDetail = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorFields,
				errorMessage);
		logger.error("errorMessage: {}", errorDetail);
		return new ResponseEntity<>(new ErrorResponse(errorDetail.getMessageCode(), errorDetail.getMessage(),
				errorDetail.getDisplayMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(AppException ex) {
		logger.error(LOG_MESSAGE, ex.getMessage(), ex.getCause(), ex);
		ErrorResponse errorDetail = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
				ex.getMessage());
		return new ResponseEntity<>(new ErrorResponse(errorDetail.getMessageCode(), errorDetail.getMessage(),
				errorDetail.getDisplayMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(HttpStatusCodeException ex) {
		logger.error(LOG_MESSAGE, ex.getMessage(), ex.getCause(), ex);
		ErrorResponse errorDetail = new ErrorResponse(ex.getRawStatusCode(), ex.getResponseBodyAsString(),
				ex.getMessage());
		return new ResponseEntity<>(
				new ErrorResponse(errorDetail.getMessageCode(), SERVER_ERROR_MESSAGE, SERVER_ERROR_MESSAGE),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(HttpServerErrorException ex) {
		logger.error(LOG_MESSAGE, ex.getMessage(), ex.getCause(), ex);
		ErrorResponse errorDetail = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
		return new ResponseEntity<>(
				new ErrorResponse(errorDetail.getMessageCode(), errorDetail.getMessage(), SERVER_ERROR_MESSAGE),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		logger.error(LOG_MESSAGE, ex.getMessage(), ex.getCause(), ex);
		ErrorResponse errorDetail = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
		return new ResponseEntity<>(
				new ErrorResponse(errorDetail.getMessageCode(), errorDetail.getMessage(), SERVER_ERROR_MESSAGE),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Throwable ex) {
		logger.error(LOG_MESSAGE, ex.getMessage(), ex.getCause(), ex);
		ErrorResponse errorDetail = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
		return new ResponseEntity<>(
				new ErrorResponse(errorDetail.getMessageCode(), errorDetail.getMessage(), SERVER_ERROR_MESSAGE),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}