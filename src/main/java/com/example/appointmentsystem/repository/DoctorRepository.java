package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByClinic_Name(String clinicName);
    List<Doctor> findByClinicId(Long clinicId);



}
