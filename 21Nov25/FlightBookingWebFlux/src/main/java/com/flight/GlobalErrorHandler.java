package com.flight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flight.exception.BusinessException;
import com.flight.exception.NotFoundException;
import com.flight.exception.SeatUnavailableException;

import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    private static final String ERROR = "error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError error : allErrors) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMap.put(field, message);
        }
        return Mono.just(ResponseEntity.badRequest().body(errorMap));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleNotFound(NotFoundException ex) {
        Map<String, String> error = Map.of(ERROR, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleSeatErrors(SeatUnavailableException ex) {
        Map<String, String> error = Map.of(ERROR, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleBusiness(BusinessException ex) {
        Map<String, String> error = Map.of(ERROR, ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGeneral(Exception ex) {
        String msg = (ex.getMessage() == null ? "" : ex.getMessage()).trim().replace("\n", " ").replace("\r", " ");
        log.error("Unexpected error: {}", msg);
        Map<String, String> error = Map.of(ERROR, "Unexpected error: " + msg);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}
