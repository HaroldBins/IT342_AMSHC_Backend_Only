package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);



    boolean existsByDoctorIdAndAppointmentStartLessThanEqualAndAppointmentEndGreaterThanEqual(
            Long doctorId,
            LocalDateTime appointmentEnd,
            LocalDateTime appointmentStart
    );



}
