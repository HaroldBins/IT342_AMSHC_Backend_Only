package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);

    // ✅ Add this:
    List<AppUser> findByRole(String role);
}
