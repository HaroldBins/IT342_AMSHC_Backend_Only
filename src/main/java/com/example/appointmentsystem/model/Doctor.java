package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gender;
    private String specialization;

    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "clinic") // Foreign key to Clinic
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)  // This maps the FK column `user_id`
    private AppUser user;

}
