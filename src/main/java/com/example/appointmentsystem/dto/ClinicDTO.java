package com.example.appointmentsystem.dto;

import lombok.Data;

@Data
public class ClinicDTO {
    private Long id;
    private String name;
    private String address;

    public ClinicDTO(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
