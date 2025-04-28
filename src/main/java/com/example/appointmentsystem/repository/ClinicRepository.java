package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    Page<Clinic> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Clinic> findByAddressContainingIgnoreCase(String address, Pageable pageable);

}
