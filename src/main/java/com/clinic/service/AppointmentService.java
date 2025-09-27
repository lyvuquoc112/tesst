package com.clinic.service;

import com.clinic.model.Appointment;
import com.clinic.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Book a new appointment
     * @param appointment Appointment object to save
     * @return saved appointment
     */
    public Appointment bookAppointment(Appointment appointment) {
        // Kiểm tra logic business nếu cần, ví dụ slot trùng, status default...
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }

    /**
     * Get all appointments for a doctor on a specific date
     * @param doctorId doctor id
     * @param date date to filter (YYYY-MM-DD)
     * @return list of appointments
     */
    public List<Appointment> getAppointmentsForDoctorOnDate(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, startOfDay, endOfDay
        );
    }
}
