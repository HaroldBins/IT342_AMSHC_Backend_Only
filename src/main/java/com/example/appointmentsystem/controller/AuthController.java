package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.dto.AuthResponse;
import com.example.appointmentsystem.dto.DoctorRegisterRequest;
import com.example.appointmentsystem.dto.LoginRequest;
import com.example.appointmentsystem.dto.LoginResponse;
import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.model.Clinic;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.repository.AppUserRepository;
import com.example.appointmentsystem.repository.ClinicRepository;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.security.CustomUserDetails;
import com.example.appointmentsystem.security.JwtUtil;
import com.example.appointmentsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final AppUserRepository appUserRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AppUser appUser) {
        String rawPassword = appUser.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        appUser.setPassword(encodedPassword);
        authService.register(appUser);
        return new AuthResponse("Registration successful");
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return new LoginResponse(token, userDetails.getRole(), userDetails.getUserId());
    }

    @PostMapping("/register-doctor")
    public ResponseEntity<String> registerDoctor(@RequestBody DoctorRegisterRequest request) {
        // 1. Create user account for doctor
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName()); // ✅ properly set full name
        user.setRole("DOCTOR");
        appUserRepository.save(user);

        // 2. Create doctor profile
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        Doctor doctor = new Doctor();
        doctor.setName(request.getFullName()); // ✅ use fullName for Doctor too
        doctor.setGender(request.getGender());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setYearsOfExperience(request.getYearsOfExperience());
        doctor.setClinic(clinic);
        doctorRepository.save(doctor);

        return ResponseEntity.ok("Doctor registered successfully.");
    }
}
