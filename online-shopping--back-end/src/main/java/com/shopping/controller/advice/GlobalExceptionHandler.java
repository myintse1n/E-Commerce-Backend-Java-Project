package com.shopping.controller.advice;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessedDeniedException(AccessDeniedException ex){
		String message = "You do not have permission to this action";
		return new ResponseEntity<>(message,HttpStatus.FORBIDDEN);
	}

}
