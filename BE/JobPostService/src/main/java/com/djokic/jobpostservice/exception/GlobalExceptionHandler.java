package com.djokic.jobpostservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<String> details = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() +": " + error.getDefaultMessage())
                .toList();

        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation Failed!")
                .path(request.getRequestURI())
                .details(details.isEmpty() ? null : details)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ){
        List<String> details = exception
                .getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();

        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation Failed!")
                .path(request.getRequestURI())
                .details(details.isEmpty() ? null : details)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request
    ){
        HttpStatus httpStatus = HttpStatus.valueOf(exception.getStatusCode().value());

        ApiError apiError = ApiError
                .builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message( exception.getReason() != null
                        ? exception.getReason()
                        : httpStatus.getReasonPhrase())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(apiError);
    }
}