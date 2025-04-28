package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.dto.ClinicDTO;
import com.example.appointmentsystem.model.Clinic;
import com.example.appointmentsystem.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    // ✅ ADD a new clinic
    @PostMapping("/add")
    public ResponseEntity<Clinic> addClinic(@RequestBody ClinicDTO dto) {
        return ResponseEntity.ok(clinicService.addClinic(dto));
    }

    // ✅ GET all clinics with pagination
    @GetMapping
    public ResponseEntity<Page<Clinic>> getAllClinics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(clinicService.getAllClinics(pageable));
    }

    // ✅ GET clinic by ID
    @GetMapping("/{id}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    // ✅ UPDATE clinic by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Clinic> updateClinic(@PathVariable Long id, @RequestBody ClinicDTO dto) {
        return ResponseEntity.ok(clinicService.updateClinic(id, dto));
    }

    // ✅ DELETE clinic + cascade delete doctors, appointments, messages
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
        return ResponseEntity.ok("Clinic and related data deleted successfully");
    }

    // ✅ SEARCH clinics by name/address
    @GetMapping("/search")
    public ResponseEntity<Page<ClinicDTO>> searchClinics(
            @RequestParam String keyword,
            @RequestParam String mode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        ));

        Page<ClinicDTO> results = clinicService.searchClinics(keyword, mode, pageable);
        return ResponseEntity.ok(results);
    }
}
