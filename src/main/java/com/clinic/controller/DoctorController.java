package com.clinic.controller;

import com.clinic.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * GET /api/doctors/{id}/availability?date=YYYY-MM-DD
     * Yêu cầu: kiểm tra token, nhận tham số động (id, date) và trả về ResponseEntity.
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<?> getAvailability(
            @PathVariable("id") Long doctorId,
            @RequestParam("date") String date,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        // Validate token rất đơn giản cho đề (nếu bạn có TokenService thì thay thế ở đây)
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token");
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date); // ISO-8601: 2025-09-27
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD.");
        }

        // ✅ kiểu dữ liệu phù hợp với service: List<LocalDateTime>
        List<LocalDateTime> slots = doctorService.getAvailableSlots(doctorId, parsedDate);
        return ResponseEntity.ok(slots);
    }
}
