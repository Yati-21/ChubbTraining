package com.chubb;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//This tells Spring that this class will handle exceptions globally
//(for all controllers), especially validation errors.
@RestControllerAdvice  
                         //tells spring if something goes wrong copme here and print msg in human readabel format
public class GlobalErrorHandler 	// This method will run whenever an exception of type Exception occurs.

{
	@ExceptionHandler(exception = Exception.class)
	public Map<String,String> handlerException(MethodArgumentNotValidException exception) //to store fieldName -> errorMessage
	{
		Map<String,String> errorMap  =new HashMap<>();  		
		List<ObjectError> errorsList=exception.getBindingResult().getAllErrors();// Collect all validation errors from the exception
		
		// Loop through every error and extract field name + error message
		errorsList.forEach((error)->
		{
			String fieldName = ((FieldError)error).getField();  //Convert ObjectError to FieldError to access the field name
			String message = error.getDefaultMessage(); // The message defined in @Valid annotations
			errorMap.put(fieldName, message);  // Put in map --> { "qty": "must be >= 1" }
			
		});
		return errorMap;  //returns a JSON response containing all field errors
		
	}

}
