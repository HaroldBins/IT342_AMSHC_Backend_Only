package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.repository.AppUserRepository;
import com.example.appointmentsystem.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AppUserRepository appUserRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        AppUser appUser = appUserRepository.findById(userDetails.getId())
                .orElse(null);

        if (appUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(appUser);
    }
}
