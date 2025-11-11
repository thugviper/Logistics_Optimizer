package com.major_project.Logistics_Optimiser.Dto;

import com.major_project.Logistics_Optimiser.Model.VehicleConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OptimizationRequest {
    @NotBlank(message = "Depot address is required")
    private String depotAddress;

    @NotEmpty(message = "At least one delivery location is required")
    private List<String> deliveryAddresses;

    private List<String> deliveryLabels;
    private VehicleConstraints constraints;

    public OptimizationRequest() {
        this.constraints = new VehicleConstraints();
    }

    // Getters and setters
    public String getDepotAddress() { return depotAddress; }
    public void setDepotAddress(String depotAddress) { this.depotAddress = depotAddress; }
    public List<String> getDeliveryAddresses() { return deliveryAddresses; }
    public void setDeliveryAddresses(List<String> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
    public List<String> getDeliveryLabels() { return deliveryLabels; }
    public void setDeliveryLabels(List<String> deliveryLabels) { this.deliveryLabels = deliveryLabels; }
    public VehicleConstraints getConstraints() { return constraints; }
    public void setConstraints(VehicleConstraints constraints) { this.constraints = constraints; }
}
