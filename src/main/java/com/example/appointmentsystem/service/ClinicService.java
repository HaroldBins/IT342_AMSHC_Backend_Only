package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.ClinicDTO;
import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.model.Clinic;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.repository.AppointmentRepository;
import com.example.appointmentsystem.repository.ClinicRepository;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageRepository messageRepository;

    // CREATE
    public Clinic addClinic(ClinicDTO dto) {
        Clinic clinic = new Clinic();
        clinic.setName(dto.getName());
        clinic.setAddress(dto.getAddress());
        return clinicRepository.save(clinic);
    }

    // READ ALL
    public Page<Clinic> getAllClinics(Pageable pageable) {
        return clinicRepository.findAll(pageable);
    }

    // READ ONE
    public Clinic getClinicById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));
    }

    // UPDATE
    public Clinic updateClinic(Long id, ClinicDTO dto) {
        Clinic clinic = getClinicById(id);
        clinic.setName(dto.getName());
        clinic.setAddress(dto.getAddress());
        return clinicRepository.save(clinic);
    }

    // DELETE + CASCADE: appointments + messages + doctors + clinic
    @Transactional
    public void deleteClinic(Long clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        List<Doctor> doctors = doctorRepository.findByClinicId(clinicId);

        for (Doctor doctor : doctors) {
            Long doctorId = doctor.getId();

            // Delete all appointments for this doctor
            appointmentRepository.deleteAll(appointmentRepository.findByDoctorId(doctorId));

            // Delete all messages for this doctor
            messageRepository.deleteBySenderIdOrReceiverId(doctorId, doctorId);
        }

        // Delete doctors under clinic
        doctorRepository.deleteAll(doctors);

        // Delete the clinic itself
        clinicRepository.delete(clinic);
    }

    // SEARCH
    public Page<ClinicDTO> searchClinics(String keyword, String mode, Pageable pageable) {
        Page<Clinic> clinics;

        if ("name".equalsIgnoreCase(mode)) {
            clinics = clinicRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if ("address".equalsIgnoreCase(mode)) {
            clinics = clinicRepository.findByAddressContainingIgnoreCase(keyword, pageable);
        } else {
            clinics = Page.empty();
        }

        return clinics.map(clinic -> new ClinicDTO(clinic.getId(), clinic.getName(), clinic.getAddress()));
    }
}
