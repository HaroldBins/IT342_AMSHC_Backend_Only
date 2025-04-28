package com.example.appointmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication
@EnableMethodSecurity
public class AppointmentSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppointmentSystemApplication.class, args);
	}
}
