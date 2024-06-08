package com.wafauserservice.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {
    
    @ExceptionHandler(WafaUserException.class)
    public ResponseEntity<ErrorDetails> handleUserException(WafaUserException userException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(userException.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WafaAdminException.class)
    public ResponseEntity<ErrorDetails> handleAdminException(WafaAdminException adminException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(adminException.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WafaSuperAdminException.class)
    public ResponseEntity<ErrorDetails> handleSuperAdminException(WafaSuperAdminException superAdminException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(superAdminException.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
