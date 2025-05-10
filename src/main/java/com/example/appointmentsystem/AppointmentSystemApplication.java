package com.example.appointmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableMethodSecurity
@EntityScan(basePackages = "com.example.appointmentsystem.model")
public class AppointmentSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentSystemApplication.class, args);
    }
}
