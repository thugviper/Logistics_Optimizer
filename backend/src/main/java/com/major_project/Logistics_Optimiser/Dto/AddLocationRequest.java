package com.major_project.Logistics_Optimiser.Dto;

import jakarta.validation.constraints.NotBlank;

public class AddLocationRequest {
    @NotBlank(message = "Address cannot be empty")
    private String address;

    private String label;

    public AddLocationRequest() {}

    public AddLocationRequest(String address, String label) {
        this.address = address;
        this.label = label;
    }

    // Getters and setters
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
