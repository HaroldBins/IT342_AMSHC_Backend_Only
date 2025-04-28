package com.example.appointmentsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private String patientName;
    private String contactNumber;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentStartTime;
    private LocalDateTime appointmentEndTime;
}
