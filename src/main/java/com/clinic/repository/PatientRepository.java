package com.clinic.repository;

import com.clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find patient by email
     * Derived query method
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Find patient by email or phone
     * Derived query method
     */
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}
