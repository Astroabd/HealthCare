package org.example.assessment.Controller;


import lombok.AllArgsConstructor;
import org.example.assessment.enums.Risk;
import org.example.assessment.service.AssessmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assessment")
@AllArgsConstructor
public class AssessmentController {
private final AssessmentService assessmentService;
    @GetMapping("patient/{patientId}")
    ResponseEntity<Risk> getRisk(@PathVariable Long patientId){
        return new ResponseEntity<>(assessmentService.getRisk(patientId), HttpStatus.OK);
    }
}
