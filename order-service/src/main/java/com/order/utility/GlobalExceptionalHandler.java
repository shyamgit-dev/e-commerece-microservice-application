package com.order.utility;

import com.order.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(RuntimeException e, HttpServletRequest request)
    {
        log.info("Exception occurred at {}:{}",
                e.getMessage(),
                request.getRequestURI(),
                e
                );

        ErrorResponse response = ErrorResponse
                .builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errorMessage("NOT_FOUND")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalException(Exception e, HttpServletRequest request)
    {
        log.info("Exception occurred at {}:{}",
                e.getMessage(),
                request.getRequestURI(),
                e
        );

         ErrorResponse response = ErrorResponse
                 .builder()
                 .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                 .errorMessage("INTERNAL_SERVER_ERROR")
                 .message(e.getMessage())
                 .timestamp(LocalDateTime.now())
                 .path(request.getRequestURI())
                 .build();
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
