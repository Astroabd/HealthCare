package org.example.notes.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp=timestamp;

    }
}
