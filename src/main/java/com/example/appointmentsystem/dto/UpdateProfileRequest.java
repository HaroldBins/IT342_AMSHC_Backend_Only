package com.example.appointmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private Long userId;
    private String fullName;
    private String email;
}
