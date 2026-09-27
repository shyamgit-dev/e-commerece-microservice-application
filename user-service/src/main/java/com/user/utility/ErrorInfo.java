package com.user.utility;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorInfo {
    private Integer status;
    private String error;
    private String path;
    private String message;
    private LocalDateTime timestamp;
}
