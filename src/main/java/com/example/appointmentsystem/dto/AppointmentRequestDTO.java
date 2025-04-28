package com.example.appointmentsystem.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequestDTO {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
}

