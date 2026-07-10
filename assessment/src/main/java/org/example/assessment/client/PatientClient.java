package org.example.assessment.client;


import org.example.patient.dto.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PATIENT")
public interface PatientClient {
     ResponseEntity<PatientResponse> findPatientById(@PathVariable Long id);
}
