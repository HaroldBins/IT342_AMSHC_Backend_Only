package com.example.appointmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private Long userId;
    private String currentPassword;
    private String newPassword;
}
