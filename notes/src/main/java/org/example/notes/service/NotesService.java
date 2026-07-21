package org.example.notes.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.notes.dto.CreateNoteDto;
import org.example.notes.dto.NotesResponse;
import org.example.notes.dto.UpdateNoteDto;
import org.example.notes.exception.NotesByIdNotFoundException;
import org.example.notes.exception.NotesByPatientIdNotFoundException;
import org.example.notes.model.Notes;
import org.example.notes.repository.NotesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotesService {
    private final NotesRepository notesRepository;


    public List<NotesResponse> findNotesByPatientId(Long patientId) {
        validatePatientById(patientId);
        List<Notes> notes= notesRepository.findNotesByPatientId(patientId);
        List<NotesResponse> notesResponses = new ArrayList<>();
        for (Notes note : notes) {
            NotesResponse response = new NotesResponse();
            response.setId(note.getId());
            response.setPatientId(note.getPatientId());
            response.setContent(note.getContent());
            response.setCreatedAt(note.getCreatedAt());
            notesResponses.add(response);
        }
        return notesResponses;

    }


    private void validatePatientById(Long patientId) {
        if (!notesRepository.existsNotesByPatientId(patientId)) {
            throw new NotesByPatientIdNotFoundException(patientId);
        }
    }


    public NotesResponse createNote(@Valid CreateNoteDto createNoteDto) {
        Notes note = new Notes();
        note.setCreatedAt(LocalDateTime.now());
        note.setPatientId(createNoteDto.getPatientId());
        note.setContent(createNoteDto.getContent());
        Notes savedNote = notesRepository.save(note);

        NotesResponse response = new NotesResponse();

        response.setId(savedNote.getId());
        response.setPatientId(savedNote.getPatientId());
        response.setContent(savedNote.getContent());
        response.setCreatedAt(savedNote.getCreatedAt());
        return response;
    }


    public NotesResponse updateNote(String id, @Valid UpdateNoteDto updateNoteDto) {
        Notes updatedNote = notesRepository.findById(id).orElseThrow(() -> new NotesByIdNotFoundException(id));
        updatedNote.setPatientId(updateNoteDto.getPatientId());
        updatedNote.setContent(updateNoteDto.getContent());
        updatedNote.setCreatedAt(LocalDateTime.now());

        Notes savedNote = notesRepository.save(updatedNote);

        NotesResponse response= new NotesResponse();
        response.setId(savedNote.getId());
        response.setPatientId(savedNote.getPatientId());
        response.setContent(savedNote.getContent());
        response.setCreatedAt(savedNote.getCreatedAt());
        return response;
    }

    public void deleteNote(String id) {
        Notes note= notesRepository.findById(id).orElseThrow(() -> new NotesByIdNotFoundException(id));
        notesRepository.delete(note);
    }

}
