package com.user.utility;

import com.user.exception.InvalidActionException;
import com.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundException(RuntimeException e, HttpServletRequest request)
    {
        ErrorInfo errorInfo = ErrorInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("NOT_FOUND")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorInfo,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<ErrorInfo> conflict(RuntimeException e, HttpServletRequest request)
    {
        ErrorInfo errorInfo = ErrorInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("CONFLICT")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorInfo,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> generalExceptionalMessage(Exception e, HttpServletRequest request)
    {
        ErrorInfo errorInfo = ErrorInfo.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("INTERNAL_SERVER_ERROR")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorInfo,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
