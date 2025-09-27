package com.clinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
    name = "doctor",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_doctor_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_doctor_phone", columnNames = "phone")
    },
    indexes = {
        @Index(name = "idx_doctor_specialty", columnList = "specialty"),
        @Index(name = "idx_doctor_full_name", columnList = "full_name")
    }
)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "specialty", nullable = false, length = 50)
    private String specialty;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Size(max = 15)
    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Phone contains invalid characters")
    @Column(name = "phone", length = 15, unique = true)
    private String phone;

    @NotBlank
    @Size(min = 60, max = 100, message = "Password hash length seems invalid")
    @JsonIgnore // tránh lộ khi trả JSON
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    /**
     * Danh sách slot khả dụng của bác sĩ.
     * Dùng Set để tránh trùng (LinkedHashSet giữ nguyên thứ tự insert).
     * JPA 3 hỗ trợ LocalDateTime trực tiếp.
     */
    @ElementCollection
    @CollectionTable(
        name = "doctor_time_slot",
        joinColumns = @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "fk_time_slot_doctor"))
    )
    @Column(name = "available_time", nullable = false)
    private Set<LocalDateTime> availableTimes = new LinkedHashSet<>();

    /** Optimistic locking để tránh ghi đè ngoài ý muốn. */
    @Version
    @Column(name = "version")
    private Long version;

    // ===== Constructors =====
    protected Doctor() { /* for JPA */ }

    public Doctor(String fullName, String specialty, String email, String phone, String passwordHash) {
        this.fullName = fullName;
        this.specialty = specialty;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
    }

    // ===== Getters / Setters =====
    public Long getDoctorId() { return doctorId; }
    // Không bắt buộc setter cho id; để bảo toàn tính bất biến ở tầng domain.
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Set<LocalDateTime> getAvailableTimes() { return availableTimes; }
    public void setAvailableTimes(Set<LocalDateTime> availableTimes) { this.availableTimes = availableTimes; }

    public Long getVersion() { return version; }
}
