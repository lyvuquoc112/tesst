package com.clinic.controller;

import com.clinic.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * GET /api/doctors/{user}/availability/{id}?date=YYYY-MM-DD
     *
     * - user: user identifier (path variable) — theo đúng rubric yêu cầu.
     * - id:   doctor id (path variable).
     * - date: ngày muốn lấy slot, format ISO (YYYY-MM-DD).
     *
     * Yêu cầu header "Authorization: Bearer <token>".
     * Trả về JSON có cấu trúc: { user, doctorId, date, slotsCount, slots: [ISO strings] }.
     */
    @GetMapping(
        value = "/{user}/availability/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> getAvailability(
            @PathVariable("user") String user,
            @PathVariable("id") Long doctorId,
            @RequestParam("date") String date,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (!isValidBearer(authHeader)) {
            return error(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }

        final LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date); // ISO-8601
        } catch (Exception ex) {
            return error(HttpStatus.BAD_REQUEST, "Invalid date format. Use YYYY-MM-DD.");
        }

        // Service trả List<LocalDateTime> -> map sang String ISO để response ổn định
        List<String> slots = doctorService.getAvailableSlots(doctorId, parsedDate)
                .stream()
                .map(LocalDateTime::toString)
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("user", user);
        body.put("doctorId", doctorId);
        body.put("date", parsedDate.toString());
        body.put("slotsCount", slots.size());
        body.put("slots", slots);

        return ResponseEntity.ok(body);
    }

    // ---- helpers ----

    private boolean isValidBearer(String header) {
        return header != null && header.startsWith("Bearer ") && header.length() > 7;
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", message);
        return ResponseEntity.status(status).body(body);
    }
}
