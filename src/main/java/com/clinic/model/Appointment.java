package com.clinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "appointment",
    uniqueConstraints = {
        // Ngăn đặt trùng lịch cho cùng 1 bác sĩ tại cùng 1 thời điểm
        @UniqueConstraint(name = "uk_doctor_time", columnNames = {"doctor_id", "appointment_time"})
    },
    indexes = {
        @Index(name = "idx_appt_doctor", columnList = "doctor_id"),
        @Index(name = "idx_appt_patient", columnList = "patient_id"),
        @Index(name = "idx_appt_time", columnList = "appointment_time")
    }
)
public class Appointment {

    public enum Status { PENDING, CONFIRMED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_appt_doctor"))
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_appt_patient"))
    private Patient patient;

    @NotNull
    @Future(message = "Appointment time must be in the future")
    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.PENDING;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    /** Optimistic locking để tránh ghi đè ngoài ý muốn. */
    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ---- Lifecycle hooks ----
    @PrePersist
    protected void onCreate() {
        final LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ---- Constructors ----
    protected Appointment() { /* for JPA */ }

    public Appointment(Doctor doctor, Patient patient,
                       LocalDateTime appointmentTime, Status status, String notes) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        if (status != null) this.status = status;
        this.notes = notes;
    }

    // ---- Getters / Setters ----
    public Long getAppointmentId() { return appointmentId; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getVersion() { return version; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
