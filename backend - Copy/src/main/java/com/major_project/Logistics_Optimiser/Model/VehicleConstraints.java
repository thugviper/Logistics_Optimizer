package com.major_project.Logistics_Optimiser.Model;

public class VehicleConstraints {
    private int numberOfVehicles;
    private int maxStopsPerRoute;
    private double maxDistancePerRoute; // in kilometers
    private int maxCapacityPerVehicle; // demand units

    public VehicleConstraints() {
        this.numberOfVehicles = 3;
        this.maxStopsPerRoute = 10;
        this.maxDistancePerRoute = 100.0;
        this.maxCapacityPerVehicle = 20;
    }

    // Getters and setters
    public int getNumberOfVehicles() { return numberOfVehicles; }
    public void setNumberOfVehicles(int numberOfVehicles) { this.numberOfVehicles = numberOfVehicles; }
    public int getMaxStopsPerRoute() { return maxStopsPerRoute; }
    public void setMaxStopsPerRoute(int maxStopsPerRoute) { this.maxStopsPerRoute = maxStopsPerRoute; }
    public double getMaxDistancePerRoute() { return maxDistancePerRoute; }
    public void setMaxDistancePerRoute(double maxDistancePerRoute) { this.maxDistancePerRoute = maxDistancePerRoute; }
    public int getMaxCapacityPerVehicle() { return maxCapacityPerVehicle; }
    public void setMaxCapacityPerVehicle(int maxCapacityPerVehicle) { this.maxCapacityPerVehicle = maxCapacityPerVehicle; }
}