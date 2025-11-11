package com.major_project.Logistics_Optimiser.Service;

import com.major_project.Logistics_Optimiser.Model.Location;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistanceMatrixService {

    private static final Logger logger = LoggerFactory.getLogger(DistanceMatrixService.class);
    private final GeoApiContext geoApiContext;

    public DistanceMatrixService(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    public double[][] calculateDistanceMatrix(List<Location> locations) {
        int n = locations.size();
        double[][] distances = new double[n][n];

        try {
            String[] origins = new String[n];
            String[] destinations = new String[n];

            for (int i = 0; i < n; i++) {
                origins[i] = locations.get(i).getLat() + "," + locations.get(i).getLng();
                destinations[i] = locations.get(i).getLat() + "," + locations.get(i).getLng();
            }

            DistanceMatrix matrix = DistanceMatrixApi.newRequest(geoApiContext)
                    .origins(origins)
                    .destinations(destinations)
                    .mode(TravelMode.DRIVING)
                    .await();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    DistanceMatrixElement element = matrix.rows[i].elements[j];
                    // Convert meters to kilometers
                    distances[i][j] = element.distance.inMeters / 1000.0;
                }
            }

            logger.info("Successfully calculated distance matrix for {} locations", n);
            return distances;

        } catch (Exception e) {
            logger.error("Error calculating distance matrix: {}", e.getMessage());
            // Fallback to Haversine formula
            return calculateHaversineMatrix(locations);
        }
    }

    private double[][] calculateHaversineMatrix(List<Location> locations) {
        int n = locations.size();
        double[][] distances = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances[i][j] = haversineDistance(
                        locations.get(i).getLat(), locations.get(i).getLng(),
                        locations.get(j).getLat(), locations.get(j).getLng()
                );
            }
        }

        return distances;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}