package com.order.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer code;
    private String errorMessage;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
