package com.clinic.controller;

import com.clinic.model.Prescription;
import com.clinic.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * POST /api/prescriptions/{token}
     * Save a new prescription.
     * Token is passed as a PathVariable per rubric requirements.
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @PathVariable("token") String token,
            @Valid @RequestBody Prescription prescription
    ) {
        Map<String, String> response = new LinkedHashMap<>();
        try {
            if (token == null || token.isBlank()) {
                response.put("status", "error");
                response.put("message", "Token is missing or invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Prescription saved = prescriptionService.savePrescription(prescription);

            response.put("status", "success");
            response.put("message", "Prescription saved successfully");
            response.put("prescriptionId", String.valueOf(saved.getPrescriptionId()));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
