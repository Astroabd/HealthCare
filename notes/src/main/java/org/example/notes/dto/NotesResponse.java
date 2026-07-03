package org.example.notes.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotesResponse {

    private String id;
    private Long patientId;
    private String Content;
    private LocalDateTime CreatedAt;

}
