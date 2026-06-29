package org.example.patient.dto;


import lombok.Getter;
import lombok.Setter;
import org.example.patient.model.Sex;

import java.time.LocalDate;

@Getter
@Setter
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Sex sex;
    private String address;
    private String phoneNumber;
}
