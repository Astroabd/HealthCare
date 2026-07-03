package org.example.notes.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateNoteDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    @NotBlank(message = "Content is required")
    private String Content;

}
