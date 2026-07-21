package org.example.assessment.client;


import org.example.assessment.dto.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PATIENT")
public interface PatientClient {
     @GetMapping("patients/{id}")
     PatientResponse findPatientById(@PathVariable Long id);
}
