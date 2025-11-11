package com.major_project.Logistics_Optimiser.Model;

import java.util.List;

public class OptimizationResult {
    private Location depot;
    private List<Route> routes;
    private double totalDistance;
    private double totalDuration;
    private int totalStops;
    private String algorithm;

    public OptimizationResult() {}

    // Getters and setters
    public Location getDepot() { return depot; }
    public void setDepot(Location depot) { this.depot = depot; }
    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public double getTotalDuration() { return totalDuration; }
    public void setTotalDuration(double totalDuration) { this.totalDuration = totalDuration; }
    public int getTotalStops() { return totalStops; }
    public void setTotalStops(int totalStops) { this.totalStops = totalStops; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}