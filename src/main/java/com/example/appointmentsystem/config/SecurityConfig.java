package com.example.appointmentsystem.config;

import com.example.appointmentsystem.security.JwtAuthenticationFilter;
import com.example.appointmentsystem.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/clinics/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/doctors").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/doctors/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register-doctor").permitAll()
                

                .requestMatchers("/api/appointments/book").authenticated()

                // Logged-in users
                .requestMatchers(HttpMethod.GET, "/api/user/me").authenticated()

                // Messaging endpoints
                .requestMatchers("/api/messages/**").authenticated()

                // Appointment access
                .requestMatchers("/api/appointments/patient/**").hasAnyRole("PATIENT", "ADMIN")
                .requestMatchers("/api/appointments/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/appointments/cancel/**")
    .hasAnyRole("PATIENT", "DOCTOR", "ADMIN")

                // Allow access to /api/appointments for ADMIN, DOCTOR, PATIENT
.requestMatchers(HttpMethod.GET, "/api/appointments").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")


                // Doctor and Clinic management
                .requestMatchers(HttpMethod.POST, "/api/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/doctors/user/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/clinics/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/clinics/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/clinics/**").hasRole("ADMIN")

                // Notifications – use exact endpoint paths!
                .requestMatchers(HttpMethod.DELETE, "/api/notifications/**")
    .hasAnyRole("PATIENT", "DOCTOR", "ADMIN")



                
.requestMatchers(HttpMethod.GET, "/api/notifications/user/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
.requestMatchers(HttpMethod.PUT, "/api/notifications/read/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
.requestMatchers(HttpMethod.PUT, "/api/notifications/read-all/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
.requestMatchers(HttpMethod.POST, "/api/notifications/create").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")


                // All other requests
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
