package com.chubb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.chubb.exception.BusinessException;
import com.chubb.exception.NotFoundException;
import com.chubb.exception.SeatUnavailableException;

public class GlobalErrorHandlerTest 
{
	private GlobalErrorHandler handler;

	@BeforeEach
    void setup() 
	{
        handler =new GlobalErrorHandler();
    }

    @Test
    void testHandleBusinessException() 
    {
    	//create a BusinessException
        BusinessException ex =new BusinessException("Business error");
        //invoke handler
        ResponseEntity<Map<String, String>> response =handler.handleBusiness(ex);
        //verify response
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("Business error",response.getBody().get("error"));
    }

    @Test
    void testHandleNotFoundException() 
    {
        NotFoundException ex = new NotFoundException("Not found");
        ResponseEntity<Map<String, String>> response=handler.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("Not found", response.getBody().get("error"));
    }

    @Test
    void testHandleSeatUnavailable() 
    {
        SeatUnavailableException ex = new SeatUnavailableException("Seat issue");
        ResponseEntity<Map<String, String>> response=handler.handleSeatErrors(ex);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("Seat issue", response.getBody().get("error"));
    }

    @Test
    void testHandleValidationErrors() 
    {
    	//create a mock MethodArgumentNotValidException
        BeanPropertyBindingResult bindingResult =new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "name", "Name is required"));
        MethodArgumentNotValidException ex=new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> response =handler.handleValidationErrors(ex);
        //verify response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Name is required", response.getBody().get("name"));
    }

    @Test
    void testHandleGeneralException() 
    {
        Exception ex=new Exception("Unexpected error happened");
        ResponseEntity<Map<String, String>> response =handler.handleGeneral(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error: Unexpected error happened",response.getBody().get("error"));
    }
}
