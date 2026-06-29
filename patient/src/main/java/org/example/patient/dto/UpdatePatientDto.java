package org.example.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.patient.model.Sex;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePatientDto {

    @NotBlank(message = "The first name is required")
    private String firstName;
    @NotBlank(message = "The last name is required")
    private String lastName;
    @Past(message = "Not valid date")
    private LocalDate dateOfBirth;
    @NotNull(message = "Sex is required (M or F)")
    private Sex sex;
    @NotBlank(message = "The address is required")
    private String address;
    @Pattern(regexp = "^\\+?[0-9 .()-]{8,20}$",
            message = "Phone number must be 8 to 20 characters long. It may optionally start with '+', and can only contain digits, spaces, dots, parentheses or hyphens. Example: +1 (123) 456-7890")
    private String phoneNumber;
}
