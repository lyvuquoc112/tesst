package com.clinic.service;

import com.clinic.model.Doctor;
import com.clinic.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Get available time slots for doctor on a given date
     */
    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            // Filter các slot trong cùng ngày
            return doctor.getAvailableTimes().stream()
                    .filter(t -> t.toLocalDate().equals(date))
                    .toList();
        }
        return List.of();
    }

    /**
     * Validate doctor login
     * @param email email của doctor
     * @param password password dạng hash (ở thực tế cần mã hóa)
     * @return ResponseEntity với thông báo
     */
    public ResponseEntity<?> validateLogin(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Doctor not found");
        }

        Doctor doctor = doctorOpt.get();
        if (!doctor.getPasswordHash().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        return ResponseEntity.ok("Login successful for Dr. " + doctor.getFullName());
    }

    /**
     * Get all doctors
     */
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }
}
