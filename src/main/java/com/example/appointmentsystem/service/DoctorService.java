package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.DoctorRequestDTO;
import com.example.appointmentsystem.dto.DoctorResponseDTO;
import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.model.Clinic;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.repository.AppointmentRepository;
import com.example.appointmentsystem.repository.ClinicRepository;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageRepository messageRepository;

    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorResponseDTO> getDoctorsByClinic(Long clinicId) {
        return doctorRepository.findByClinicId(clinicId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public DoctorResponseDTO addDoctor(DoctorRequestDTO dto) {
        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setGender(dto.getGender());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setClinic(clinic);

        return convertToDTO(doctorRepository.save(doctor));
    }

    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        doctor.setName(dto.getName());
        doctor.setGender(dto.getGender());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setClinic(clinic);

        return convertToDTO(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 1. Delete appointments linked to the doctor
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        appointmentRepository.deleteAll(appointments);

        // 2. Delete messages sent or received by the doctor
        messageRepository.deleteBySenderIdOrReceiverId(doctorId, doctorId);

        // 3. Delete the doctor
        doctorRepository.delete(doctor);
    }

    private DoctorResponseDTO convertToDTO(Doctor doctor) {
        return new DoctorResponseDTO(
            doctor.getId(),
            doctor.getUser() != null ? doctor.getUser().getId() : null,
            doctor.getName(),
            doctor.getGender(),
            doctor.getSpecialization(),
            doctor.getYearsOfExperience(),
            doctor.getClinic().getId(),
            doctor.getClinic().getName(),
            doctor.getClinic().getAddress(),
            doctor.getUser() != null ? doctor.getUser().getAvatarUrl() : null // 🆕 Added here
        );
    }
    
    
    
}
