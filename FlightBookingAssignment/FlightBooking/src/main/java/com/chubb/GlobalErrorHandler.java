package com.chubb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

	
	// used to handle @Valid validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {

        Map<String, String> errorMap = new HashMap<>();

        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();

        for (ObjectError error : allErrors) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMap.put(field, message);
        }

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    // handles RuntimeExceptions (like cancel/update rules)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        String msg = ex.getMessage();
        error.put("error", msg == null ? "Unexpected error occurred" : msg);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}