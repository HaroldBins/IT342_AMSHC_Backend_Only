package com.example.appointmentsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthStatusController {

    @GetMapping("/success")
    public String loginSuccess() {
        return "Login successful!";
    }
}
