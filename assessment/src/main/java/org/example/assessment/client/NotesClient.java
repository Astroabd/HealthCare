package org.example.assessment.client;

import org.example.assessment.dto.NotesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("NOTES")
public interface NotesClient {
    @GetMapping("notes/patient/{patientId}")
     List<NotesResponse> findNotesByPatientId(@PathVariable Long patientId);
}
