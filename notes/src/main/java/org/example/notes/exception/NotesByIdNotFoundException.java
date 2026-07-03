package org.example.notes.exception;

public class NotesByIdNotFoundException extends RuntimeException {
    public NotesByIdNotFoundException(String id) {
        super("Notes with id " + id + " not found");
    }}
