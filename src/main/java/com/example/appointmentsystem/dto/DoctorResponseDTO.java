package com.example.appointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private Long userId;
    private String name;
    private String gender;
    private String specialization;
    private int yearsOfExperience;
    private Long clinicId;
    private String clinicName;
    private String clinicAddress;
    private String avatarUrl;   // 🆕 Added
}
