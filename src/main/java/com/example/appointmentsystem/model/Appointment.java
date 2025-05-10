package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
@JsonBackReference
private Doctor doctor;


@ManyToOne
@JoinColumn(name = "patient_id")
private AppUser patient;

    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;


    private String status;
}
