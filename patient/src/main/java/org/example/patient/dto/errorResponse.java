package org.example.patient.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class errorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public errorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp=timestamp;

    }
}
