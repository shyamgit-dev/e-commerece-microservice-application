package com.product.utility;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String errorMessage;
    private Integer code;
    private LocalDateTime timestamp;
    private String message;
    private String path;
}
