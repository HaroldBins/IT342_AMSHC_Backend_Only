package com.example.appointmentsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.appointmentsystem.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByClinic_Name(String clinicName);
    List<Doctor> findByClinicId(Long clinicId);

    void deleteByUserId(Long userId);




}
