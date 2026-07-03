package org.example.notes.exception;

public class NotesByPatientIdNotFoundException extends RuntimeException {
    public NotesByPatientIdNotFoundException(Long patientId) {
        super("Notes not found for patientId: " + patientId);
    }
}
