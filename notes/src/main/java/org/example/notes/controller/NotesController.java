package org.example.notes.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.notes.dto.CreateNoteDto;
import org.example.notes.dto.NotesResponse;
import org.example.notes.dto.UpdateNoteDto;
import org.example.notes.service.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {
    private final NotesService notesService;

    //it this endpoint gives all the notes that are related to the patientId
    @GetMapping("/{patientId}")
    public ResponseEntity<List<NotesResponse>> findNotesByPatientId(@PathVariable Long patientId){
        return new ResponseEntity<>(notesService.findNotesByPatientId(patientId), HttpStatus.OK);
    }


    // this endpoint is used to create a note requiring a patientId and a note content, it will return the created note with its id and the patientId
    @PostMapping
    public ResponseEntity<NotesResponse> createNote(@RequestBody @Valid CreateNoteDto createNoteDto){
        return new ResponseEntity<>(notesService.createNote(createNoteDto), HttpStatus.CREATED);
    }
    //this endpoint is used to update a note id
    @PutMapping("/{id}")
    public ResponseEntity<NotesResponse> updateNote(@PathVariable String id,
                                                    @RequestBody @Valid UpdateNoteDto updateNoteDto){
        return new ResponseEntity<>(notesService.updateNote(id, updateNoteDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id){
        notesService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }



}
