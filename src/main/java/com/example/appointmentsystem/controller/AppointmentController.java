package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.dto.AppointmentRequestDTO;
import com.example.appointmentsystem.dto.AppointmentResponseDTO;
import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    
    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequestDTO dto) {
        Appointment appointment = appointmentService.bookAppointment(dto);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(id));
    }

    @GetMapping("/doctor/{id}")
public ResponseEntity<List<AppointmentResponseDTO>> getByDoctor(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorDTO(id));
}


@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
@GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
}
