package com.example.appointmentsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private String doctorName;    // for patient viewing
    private String patientName;   // for doctor viewing
    private String specialization;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private String status;
}
