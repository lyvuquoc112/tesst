package com.clinic.controller;

import com.clinic.model.Doctor;
import com.clinic.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // GET endpoint: /api/doctors/{id}/availability?date=2025-10-01
    @GetMapping("/{id}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable Long id,
            @RequestParam String date,
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Validate token (giả lập, bạn sẽ thay bằng JWT service thật)
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid token");
            }

            LocalDate parsedDate = LocalDate.parse(date);
            List<String> availableSlots = doctorService.getAvailableSlots(id, parsedDate);

            return ResponseEntity.ok().body(availableSlots);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // Ví dụ: GET /api/doctors -> danh sách bác sĩ
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.findAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}
