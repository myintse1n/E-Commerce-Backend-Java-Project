package com.shopping.controller.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<String> handleAccessedDeniedException(AuthorizationDeniedException ex){
		String message = "You do not have permission to this action";
		return new ResponseEntity<>(message,HttpStatus.FORBIDDEN);
	}

}
