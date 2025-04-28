package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.*;
import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.repository.AppUserRepository;
import com.example.appointmentsystem.security.JwtUtil;
import com.example.appointmentsystem.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        AppUser user = appUserRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new LoginResponse(token, user.getRole(), user.getId(), user.getFullName(), user.getProfilePictureUrl());
    }

    public void register(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public void sendResetCode(String email) {
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isEmpty()) return;
        AppUser user = userOpt.get();
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setResetCode(code);
        appUserRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Password Reset Code", "Your reset code is: " + code);
    }

    public boolean resetPassword(String email, String code, String newPassword) {
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;
        AppUser user = userOpt.get();
        if (!code.equals(user.getResetCode())) return false;
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        appUserRepository.save(user);
        return true;
    }

    public boolean updateProfile(UpdateProfileRequest request) {
        Optional<AppUser> userOpt = appUserRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) return false;
        AppUser user = userOpt.get();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        appUserRepository.save(user);
        return true;
    }

    public boolean changePassword(ChangePasswordRequest request) {
        Optional<AppUser> userOpt = appUserRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) return false;
        AppUser user = userOpt.get();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) return false;
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        appUserRepository.save(user);
        return true;
    }

    public AppUser getUserProfile(Long userId) {
        return appUserRepository.findById(userId).orElse(null);
    }

    public boolean deleteAccount(Long userId) {
        if (appUserRepository.existsById(userId)) {
            appUserRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public String saveAvatar(Long userId, MultipartFile file) throws IOException {
        String uploadDir = "uploads/avatars/";
        String fileName = userId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        user.setProfilePictureUrl(filePath.toString().replace("\\", "/"));
        appUserRepository.save(user);
        return fileName;
    }
}
