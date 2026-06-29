package org.example.patient.exception;

public class PatientALreadyExistsException extends RuntimeException {
    public PatientALreadyExistsException(String firstName, String lastName, String phoneNumber) {
        super("Patient with the same first name " + firstName + ", last name " + lastName + ", and phone number " + phoneNumber + " already exists.");
    }
}
