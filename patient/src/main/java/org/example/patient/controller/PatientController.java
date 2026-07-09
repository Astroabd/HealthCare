package org.example.patient.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.patient.dto.PatientResponse;
import org.example.patient.dto.CreatePatientDto;
import org.example.patient.dto.UpdatePatientDto;
import org.example.patient.model.Patient;
import org.example.patient.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/patients")    
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    //Get a list of all the patients in the DB
    @GetMapping
    public ResponseEntity<List<Patient>> findAllPatients() {
        return ResponseEntity.ok(patientService.findAllPatients());
    }


    //Get the patient from DB by id if the patient doesn't exist, return a 404 error with a message "Patient not found with id: {id} not found"
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findPatientById(id));
    }

    //Create a new patient by entering a body with specific fields I specified by CreatePatientDto form first the values get checked by the annotations I used ,
    //sending a custom message if the value isnt entered correctly then sending a PatientReponse and an Http status
    @PostMapping
    public ResponseEntity<PatientResponse> createNewPatient(@RequestBody @Valid CreatePatientDto createPatientDto) {
        return new ResponseEntity<>(patientService.createPatient(createPatientDto) , HttpStatus.CREATED);

    }

    //modifying a patient by their id, first we assure if the patient by this id exists ,if not throws a PatientNotFoundException, u can modify its fields
    //and as the create new patient method each value needs to be checked then it returns the PatientResponse and an HTtp status
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id,
                                                         @RequestBody @Valid UpdatePatientDto updatePatientDto){
        return new ResponseEntity<>(patientService.updatePatientById(id, updatePatientDto), HttpStatus.OK);
    }

    //it deletes patient by the id but before anything plays out we need to verfiy if the patient with this id actually exists if yes it proceeds to delete the patient
    // returning a noContent http status which is 204 meaning theres no content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }




}
