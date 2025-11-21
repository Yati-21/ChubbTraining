package com.chubb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chubb.exception.BusinessException;
import com.chubb.exception.NotFoundException;
import com.chubb.exception.SeatUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
class GlobalErrorHandler 
{
	private static final String ERROR="error";

	//used to handle @Valid validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException exception) 
    {
        Map<String,String> errorMap =new HashMap<>();
        List<ObjectError> allErrors =exception.getBindingResult().getAllErrors();
        //iterate through errors and populate map
        for (ObjectError error:allErrors) 
        {
            String field =((FieldError) error).getField();
            String message= error.getDefaultMessage();
            errorMap.put(field,message);
        }
        return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException ex) 
    {
        Map<String,String> error=Map.of(ERROR,ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleSeatErrors(SeatUnavailableException ex) 
    {
        Map<String,String> error=Map.of(ERROR, ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusiness(BusinessException ex) 
    {
        Map<String,String> error=Map.of(ERROR,ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) 
    {
        String msg=(ex.getMessage()== null ? "":ex.getMessage()).trim().replace("\n"," ").replace("\r"," ");
        log.error("Unexpected error: {}", msg); 
        Map<String,String> error=Map.of(ERROR, "Unexpected error: " + msg);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}