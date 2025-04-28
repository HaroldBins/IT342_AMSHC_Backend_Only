package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.LoginRequest;
import com.example.appointmentsystem.dto.LoginResponse;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return new LoginResponse(
                token,
                userDetails.getAppUser().getRole(),
                userDetails.getAppUser().getId()
        );
    }

    public void register(AppUser appUser) {
        appUserRepository.save(appUser);
    }
}
