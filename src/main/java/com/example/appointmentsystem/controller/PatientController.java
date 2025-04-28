package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllPatients() {
        return ResponseEntity.ok(appUserRepository.findByRole("PATIENT"));
    }
}
