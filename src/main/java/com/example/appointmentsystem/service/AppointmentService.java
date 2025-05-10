package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.AppointmentRequestDTO;
import com.example.appointmentsystem.dto.AppointmentResponseDTO;
import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.model.NotificationType;
import com.example.appointmentsystem.repository.AppUserRepository;
import com.example.appointmentsystem.repository.AppointmentRepository;
import com.example.appointmentsystem.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final NotificationService notificationService;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppUserRepository appUserRepository;

    public Appointment bookAppointment(AppointmentRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        AppUser patient = appUserRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        boolean isOverlapping = appointmentRepository
                .existsByDoctorIdAndAppointmentStartLessThanEqualAndAppointmentEndGreaterThanEqual(
                        doctor.getId(),
                        dto.getAppointmentEnd(),
                        dto.getAppointmentStart()
                );

        if (isOverlapping) {
            throw new RuntimeException("The appointment time is already booked.");
        }

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentStart(dto.getAppointmentStart())
                .appointmentEnd(dto.getAppointmentEnd())
                .status("CONFIRMED")
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        String formattedStart = savedAppointment.getAppointmentStart().format(formatter);

        notificationService.createNotification(
                patient.getId(),
                "Your appointment with Dr. " + doctor.getName() + " is confirmed for " + formattedStart + ".",
                NotificationType.APPOINTMENT_REMINDER
        );

        notificationService.createNotification(
                doctor.getId(),
                "You have a new appointment with " + patient.getFullName() + " on " + formattedStart + ".",
                NotificationType.APPOINTMENT_REMINDER
        );
System.out.println("👉 Booking request for patientId: " + dto.getPatientId());
System.out.println("👉 DoctorId: " + dto.getDoctorId());
System.out.println("👉 Authenticated user: " + SecurityContextHolder.getContext().getAuthentication());

        return savedAppointment;
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Doctor doctor = appointment.getDoctor();
        AppUser patient = appointment.getPatient();

        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        String formattedStart = appointment.getAppointmentStart().format(formatter);

        notificationService.createNotification(
                patient.getId(),
                "Your appointment on " + formattedStart + " was cancelled.",
                NotificationType.CANCELLATION
        );

        notificationService.createNotification(
                doctor.getId(),
                "Appointment with " + patient.getFullName() + " on " + formattedStart + " was cancelled.",
                NotificationType.CANCELLATION
        );
    }

    public List<AppointmentResponseDTO> getAppointmentsByDoctorDTO(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        System.out.println("👉 Appointments fetched for doctorId=" + doctorId + ": " + appointments.size());
    
        appointments.forEach(a ->
            System.out.println("🗓️ " + a.getAppointmentStart() + " with " + a.getPatient().getFullName())
        );
    
        return appointments.stream()
            .map(appt -> {
                AppointmentResponseDTO dto = new AppointmentResponseDTO();
                dto.setId(appt.getId());
                dto.setPatientName(appt.getPatient().getFullName());
                dto.setSpecialization(appt.getDoctor().getSpecialization());
                dto.setAppointmentStart(appt.getAppointmentStart());
                dto.setAppointmentEnd(appt.getAppointmentEnd());
                dto.setStatus(appt.getStatus());
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    

    public List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
    
        return appointments.stream().map(appt -> {
            AppointmentResponseDTO dto = new AppointmentResponseDTO();
            dto.setId(appt.getId());
            dto.setDoctorName(appt.getDoctor().getName());
            dto.setSpecialization(appt.getDoctor().getSpecialization());
            dto.setAppointmentStart(appt.getAppointmentStart());
            dto.setAppointmentEnd(appt.getAppointmentEnd());
            dto.setStatus(appt.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
    
    

    
    

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
