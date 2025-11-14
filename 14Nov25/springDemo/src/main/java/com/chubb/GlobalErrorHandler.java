package com.chubb;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  //tells spring if something goes wrong copme here and print msg in human readabel format
public class GlobalErrorHandler 
{
	@ExceptionHandler(exception = Exception.class)
	public Map<String,String> handlerException(MethodArgumentNotValidException exception) 
	{
		Map<String,String> errorMap  =new HashMap<>();
		List<ObjectError> errorsList=exception.getBindingResult().getAllErrors();	
		errorsList.forEach((error)->
		{
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			errorMap.put(fieldName, message);
			
		});
		return errorMap;
	}

}
