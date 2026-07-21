package org.example.assessment.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.assessment.enums.Sex;

import java.time.LocalDate;

@Getter
@Setter
public class PatientResponse {
    private Sex sex;
    private LocalDate dateOfBirth;
}
