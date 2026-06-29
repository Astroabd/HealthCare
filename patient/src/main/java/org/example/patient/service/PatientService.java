package org.example.patient.service;


import lombok.RequiredArgsConstructor;
import org.example.patient.dto.PatientResponse;
import org.example.patient.dto.CreatePatientDto;
import org.example.patient.dto.UpdatePatientDto;
import org.example.patient.exception.PatientALreadyExistsException;
import org.example.patient.exception.PatientNotFoundException;
import org.example.patient.model.Patient;
import org.example.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    public PatientResponse findPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(patient.getId());
        patientResponse.setFirstName(patient.getFirstName());
        patientResponse.setLastName(patient.getLastName());
        patientResponse.setDateOfBirth(patient.getDateOfBirth());
        patientResponse.setSex(patient.getSex());
        patientResponse.setAddress(patient.getAddress());
        patientResponse.setPhoneNumber(patient.getPhoneNumber());
        return patientResponse;
    }


    public PatientResponse createPatient(CreatePatientDto createPatientDto) {
       validatePatientNotExists(createPatientDto.getFirstName(), createPatientDto.getLastName(), createPatientDto.getPhoneNumber());
       Patient patient = new Patient();
       patient.setFirstName(createPatientDto.getFirstName());
       patient.setLastName(createPatientDto.getLastName());
       patient.setDateOfBirth(createPatientDto.getDateOfBirth());
       patient.setSex(createPatientDto.getSex());
       patient.setAddress(createPatientDto.getAddress());
       patient.setPhoneNumber(createPatientDto.getPhoneNumber());

       Patient saved = patientRepository.save(patient);

       PatientResponse patientResponse = new PatientResponse();
       patientResponse.setId(saved.getId());
       patientResponse.setFirstName(saved.getFirstName());
       patientResponse.setLastName(saved.getLastName());
       patientResponse.setDateOfBirth(saved.getDateOfBirth());
       patientResponse.setSex(saved.getSex());
       patientResponse.setPhoneNumber(saved.getPhoneNumber());
       patientResponse.setAddress(saved.getAddress());


       return patientResponse;
    }


    private void validatePatientNotExists(String firstName, String lastName, String phoneNumber){
        if(patientRepository.existsByFirstNameAndLastNameAndPhoneNumber(firstName, lastName, phoneNumber)) {
            throw new PatientALreadyExistsException(firstName, lastName, phoneNumber);}
    }

    public PatientResponse updatePatientById(Long id, UpdatePatientDto updatePatientDto) {
        Patient updated = patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException(id));
        updated.setFirstName(updatePatientDto.getFirstName());
        updated.setLastName(updatePatientDto.getLastName());
        updated.setDateOfBirth(updatePatientDto.getDateOfBirth());
        updated.setSex(updatePatientDto.getSex());
        updated.setAddress(updatePatientDto.getAddress());
        updated.setPhoneNumber(updatePatientDto.getPhoneNumber());

        Patient saved = patientRepository.save(updated);

        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(saved.getId());
        patientResponse.setFirstName(saved.getFirstName());
        patientResponse.setLastName(saved.getLastName());
        patientResponse.setDateOfBirth(saved.getDateOfBirth());
        patientResponse.setSex(saved.getSex());
        patientResponse.setPhoneNumber(saved.getPhoneNumber());
        patientResponse.setAddress(saved.getAddress());

        return patientResponse;
    }


    public void deletePatient(Long id) {
        Patient patient= patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException(id));
        patientRepository.delete(patient);

    }
}
