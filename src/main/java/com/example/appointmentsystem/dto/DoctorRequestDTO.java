package com.example.appointmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRequestDTO {
    private String name;
    private String gender;
    private String specialization;

    @JsonProperty("yearsOfExperience")
    private int yearsOfExperience;

    private Long clinicId;
}

