package com.major_project.Logistics_Optimiser.Service;

import com.major_project.Logistics_Optimiser.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteOptimizationService {

    private static final Logger logger = LoggerFactory.getLogger(RouteOptimizationService.class);
    private final DistanceMatrixService distanceMatrixService;

    public RouteOptimizationService(DistanceMatrixService distanceMatrixService) {
        this.distanceMatrixService = distanceMatrixService;
    }

    public OptimizationResult optimizeRoutes(Location depot, List<Location> deliveryLocations, VehicleConstraints constraints) {
        logger.info("Starting route optimization for {} delivery locations", deliveryLocations.size());

        // Create full location list (depot + deliveries)
        List<Location> allLocations = new ArrayList<>();
        allLocations.add(depot);
        allLocations.addAll(deliveryLocations);

        // Calculate distance matrix
        double[][] distanceMatrix = distanceMatrixService.calculateDistanceMatrix(allLocations);

        // Apply optimization algorithm
        List<Route> routes = clusterAndOptimize(depot, deliveryLocations, distanceMatrix, constraints);

        // Calculate route distances
        routes = calculateRouteMetrics(routes, distanceMatrix, allLocations);

        // Prepare result
        OptimizationResult result = new OptimizationResult();
        result.setDepot(depot);
        result.setRoutes(routes);
        result.setAlgorithm("Nearest Neighbor with Clustering");

        double totalDistance = routes.stream().mapToDouble(Route::getTotalDistance).sum();
        double totalDuration = routes.stream().mapToDouble(Route::getTotalDuration).sum();
        int totalStops = routes.stream().mapToInt(r -> r.getStops().size()).sum();

        result.setTotalDistance(totalDistance);
        result.setTotalDuration(totalDuration);
        result.setTotalStops(totalStops);

        logger.info("Optimization complete. Total distance: {:.2f} km across {} routes", totalDistance, routes.size());

        return result;
    }

    private List<Route> clusterAndOptimize(Location depot, List<Location> deliveries,
                                           double[][] distanceMatrix, VehicleConstraints constraints) {
        List<Route> routes = new ArrayList<>();
        List<Location> unassigned = new ArrayList<>(deliveries);

        int numVehicles = Math.min(constraints.getNumberOfVehicles(),
                (int) Math.ceil((double) deliveries.size() / constraints.getMaxStopsPerRoute()));

        // Use K-means style clustering
        for (int v = 0; v < numVehicles && !unassigned.isEmpty(); v++) {
            Route route = new Route(v + 1);
            route.addStop(depot); // Start at depot

            // Select seed location (furthest from depot among remaining)
            Location seed = selectSeedLocation(depot, unassigned);
            if (seed != null) {
                route.addStop(seed);
                unassigned.remove(seed);
            }

            // Build route using nearest neighbor
            while (!unassigned.isEmpty() && route.getStops().size() < constraints.getMaxStopsPerRoute() + 1) {
                Location current = route.getStops().get(route.getStops().size() - 1);
                Location nearest = findNearestLocation(current, unassigned, distanceMatrix, deliveries, depot);

                if (nearest != null) {
                    route.addStop(nearest);
                    unassigned.remove(nearest);
                } else {
                    break;
                }
            }

            route.addStop(depot); // Return to depot
            routes.add(route);
        }

        // Assign any remaining locations to nearest route
        while (!unassigned.isEmpty()) {
            Location loc = unassigned.remove(0);
            Route nearestRoute = findNearestRoute(loc, routes);
            if (nearestRoute != null && nearestRoute.getStops().size() < constraints.getMaxStopsPerRoute() + 2) {
                // Insert before returning to depot
                nearestRoute.getStops().add(nearestRoute.getStops().size() - 1, loc);
            }
        }

        return routes;
    }

    private Location selectSeedLocation(Location depot, List<Location> candidates) {
        if (candidates.isEmpty()) return null;

        Location furthest = null;
        double maxDistance = 0;

        for (Location loc : candidates) {
            double dist = calculateDistance(depot.getLat(), depot.getLng(), loc.getLat(), loc.getLng());
            if (dist > maxDistance) {
                maxDistance = dist;
                furthest = loc;
            }
        }

        return furthest;
    }

    private Location findNearestLocation(Location current, List<Location> candidates,
                                         double[][] distanceMatrix, List<Location> allDeliveries, Location depot) {
        if (candidates.isEmpty()) return null;

        Location nearest = null;
        double minDistance = Double.MAX_VALUE;

        // Find indices
        List<Location> allLocs = new ArrayList<>();
        allLocs.add(depot);
        allLocs.addAll(allDeliveries);

        int currentIdx = allLocs.indexOf(current);

        for (Location candidate : candidates) {
            int candidateIdx = allLocs.indexOf(candidate);
            double dist = distanceMatrix[currentIdx][candidateIdx];

            if (dist < minDistance) {
                minDistance = dist;
                nearest = candidate;
            }
        }

        return nearest;
    }

    private Route findNearestRoute(Location location, List<Route> routes) {
        if (routes.isEmpty()) return null;

        Route nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Route route : routes) {
            if (route.getStops().size() >= 2) {
                Location routeCenter = route.getStops().get(1); // First delivery point
                double dist = calculateDistance(location.getLat(), location.getLng(),
                        routeCenter.getLat(), routeCenter.getLng());
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = route;
                }
            }
        }

        return nearest != null ? nearest : routes.get(0);
    }

    private List<Route> calculateRouteMetrics(List<Route> routes, double[][] distanceMatrix, List<Location> allLocations) {
        for (Route route : routes) {
            double totalDist = 0.0;
            List<Location> stops = route.getStops();

            for (int i = 0; i < stops.size() - 1; i++) {
                int idx1 = allLocations.indexOf(stops.get(i));
                int idx2 = allLocations.indexOf(stops.get(i + 1));
                totalDist += distanceMatrix[idx1][idx2];
            }

            route.setTotalDistance(totalDist);
            route.setTotalDuration(totalDist / 40.0 * 60); // Assume 40 km/h average speed, convert to minutes
        }

        return routes;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
