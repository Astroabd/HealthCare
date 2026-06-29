package org.example.patient.exception;


import org.example.patient.dto.errorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<errorResponse> handleException(Exception ex){
        errorResponse response= new errorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "We've encountered an unexpected error", LocalDateTime.now() );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({PatientNotFoundException.class})
    public ResponseEntity<errorResponse> handlePatientNotFoundException(PatientNotFoundException ex){
        errorResponse response = new errorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({PatientALreadyExistsException.class})
    public ResponseEntity<errorResponse> handlePatientAlreadyExistsException(PatientALreadyExistsException ex){
        errorResponse response = new errorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<errorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {


        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        errorResponse response = new errorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


}
