package org.example.assessment.service;

import lombok.RequiredArgsConstructor;
import org.example.assessment.client.NotesClient;
import org.example.assessment.client.PatientClient;
import org.example.assessment.dto.NotesResponse;
import org.example.assessment.dto.PatientResponse;
import org.example.assessment.enums.Risk;
import org.example.assessment.enums.Sex;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final NotesClient notesClient;
    private final PatientClient patientClient;
    private static final List<String> TRIGGER_TERMS = List.of(
            "Hémoglobine A1C", "Microalbumine", "Taille", "Poids",
            "Fumeur", "Fumeuse", "Anormal", "Cholestérol",
            "Vertige", "Rechute", "Réaction", "Anticorps"
    );

    public Risk getRisk(Long patientId) {


        PatientResponse patient = patientClient.findPatientById(patientId);
        List<NotesResponse> notes = notesClient.findNotesByPatientId(patientId);
        int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
        int triggerCount = countTriggerTerms(notes);
        return calculateRisk(age, patient.getSex(), triggerCount);
    }

    private int countTriggerTerms(List<NotesResponse> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0;}
        String allContent = notes.stream()
                .map(NotesResponse::getContent)
                .collect(Collectors.joining(" "))
                .toLowerCase();
        int count = 0;
        for (String term : TRIGGER_TERMS) {
            if (allContent.contains(term.toLowerCase())) {
                count++;}
        }
        return count;
    }
    private Risk calculateRisk(int age, Sex sex, int triggerCount) {

        if (age > 30) {
            if (triggerCount >= 8) return Risk.EARLY_ONSET;
            if (triggerCount >= 6) return Risk.IN_DANGER;
            if (triggerCount >= 2) return Risk.BORDERLINE;
            return Risk.NONE;
        }
        if (sex == Sex.M) {
            if (triggerCount >= 5) return Risk.EARLY_ONSET;
            if (triggerCount >= 3) return Risk.IN_DANGER;
            return Risk.NONE;
        }
        if (triggerCount >= 7) return Risk.EARLY_ONSET;
        if (triggerCount >= 4) return Risk.IN_DANGER;
        return Risk.NONE;
    }
}
