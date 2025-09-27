package com.clinic.controller;

import com.clinic.model.Prescription;
import com.clinic.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * POST /api/prescriptions
     * Save a new prescription
     */
    @PostMapping
    public ResponseEntity<?> createPrescription(
            @Valid @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Validate token (demo check, bạn thay bằng JWT service thực tế)
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid token");
            }

            Prescription saved = prescriptionService.savePrescription(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}
