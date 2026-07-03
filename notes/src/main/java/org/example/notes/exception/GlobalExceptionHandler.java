package org.example.notes.exception;


import org.example.notes.dto.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        ErrorResponse response= new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "We've encountered an unexpected error", LocalDateTime.now() );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({NotesByPatientIdNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotesByPatientIdNotFoundException(
            NotesByPatientIdNotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value()
                ,ex.getMessage(), LocalDateTime.now() );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotesByIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotesByIdNotFoundException(
            NotesByIdNotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {


        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
