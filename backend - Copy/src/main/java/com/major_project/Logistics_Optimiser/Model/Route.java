package com.major_project.Logistics_Optimiser.Model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private int routeNumber;
    private List<Location> stops;
    private double totalDistance;
    private double totalDuration;
    private int totalDemand;

    public Route(int routeNumber) {
        this.routeNumber = routeNumber;
        this.stops = new ArrayList<>();
        this.totalDistance = 0.0;
        this.totalDuration = 0.0;
        this.totalDemand = 0;
    }

    public void addStop(Location location) {
        stops.add(location);
        totalDemand += location.getDemand();
    }

    // Getters and setters
    public int getRouteNumber() { return routeNumber; }
    public void setRouteNumber(int routeNumber) { this.routeNumber = routeNumber; }
    public List<Location> getStops() { return stops; }
    public void setStops(List<Location> stops) { this.stops = stops; }
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public double getTotalDuration() { return totalDuration; }
    public void setTotalDuration(double totalDuration) { this.totalDuration = totalDuration; }
    public int getTotalDemand() { return totalDemand; }
    public void setTotalDemand(int totalDemand) { this.totalDemand = totalDemand; }
}
