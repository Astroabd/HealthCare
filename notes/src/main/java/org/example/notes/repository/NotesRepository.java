package org.example.notes.repository;

import org.example.notes.model.Notes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends MongoRepository<Notes,String> {
    boolean existsNotesByPatientId(Long patientId);

     List<Notes> findNotesByPatientId(Long patientId);
}
