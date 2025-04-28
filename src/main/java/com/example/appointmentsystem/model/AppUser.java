package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")  // 🛠 Avoids PostgreSQL keyword conflict
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String role;
}
