package com.example.demo.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {
	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationErrors(MethodArgumentNotValidException exception) 
	{

        Map<String, String> errorMap = new HashMap<>();

        List<ObjectError> errors = exception.getBindingResult().getAllErrors();

        errors.forEach(error -> {
            String field = ((FieldError) error).getField();   
            String message = error.getDefaultMessage(); 
            errorMap.put(field, message);
        });

        return errorMap;
    }

}
