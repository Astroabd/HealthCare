package org.example.assessment.client;

import org.example.notes.dto.NotesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("NOTES")
public interface NotesClient {
    @GetMapping("/patient/{patientId}")
     ResponseEntity<List<NotesResponse>> findNotesByPatientId(@PathVariable Long patientId);
}
