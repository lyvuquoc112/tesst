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
     * - Nhận id, date (tham số động)
     * - Kiểm tra token đơn giản
     * - Trả về danh sách slot dưới dạng String (ISO) để tránh lỗi kiểu dữ liệu
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<String>> getAvailability(
            @PathVariable("id") Long doctorId,
            @RequestParam("date") String date,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date); // định dạng YYYY-MM-DD
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        // Service trả List<LocalDateTime> -> chuyển sang List<String> để khớp kiểu
        List<String> slots = doctorService.getAvailableSlots(doctorId, parsedDate)
                .stream()
                .map(LocalDateTime::toString) // ví dụ "2025-09-27T09:30"
                .toList();

        return ResponseEntity.ok(slots);
    }
}
