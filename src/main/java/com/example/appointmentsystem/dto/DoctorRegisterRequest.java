package com.example.appointmentsystem.dto;

import lombok.Data;

@Data
public class DoctorRegisterRequest {
    // 🔒 User Account Info
    private String email;
    private String password;
    private String fullName;

    // 👨‍⚕️ Doctor Profile Info
    private String gender;
    private String specialization;
    private int yearsOfExperience;
    private Long clinicId;
}
