package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.dto.DoctorRequestDTO;
import com.example.appointmentsystem.dto.DoctorResponseDTO;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<DoctorResponseDTO>> getByClinic(@PathVariable Long clinicId) {
        return ResponseEntity.ok(doctorService.getDoctorsByClinic(clinicId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<DoctorResponseDTO> addDoctor(@RequestBody DoctorRequestDTO dto) {
        return ResponseEntity.ok(doctorService.addDoctor(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorRequestDTO dto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    // ✅ Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return ResponseEntity.ok(doctor);
    }

}
