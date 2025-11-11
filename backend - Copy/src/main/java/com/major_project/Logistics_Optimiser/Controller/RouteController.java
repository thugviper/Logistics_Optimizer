package com.major_project.Logistics_Optimiser.Controller;

import com.major_project.Logistics_Optimiser.Dto.ApiResponse;
import com.major_project.Logistics_Optimiser.Dto.OptimizationRequest;
import com.major_project.Logistics_Optimiser.Model.Location;
import com.major_project.Logistics_Optimiser.Model.OptimizationResult;
import com.major_project.Logistics_Optimiser.Service.GeocodingService;
import com.major_project.Logistics_Optimiser.Service.RouteOptimizationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);

    private final GeocodingService geocodingService;
    private final RouteOptimizationService routeOptimizationService;
    private final String mapsApiKey;

    public RouteController(GeocodingService geocodingService,
                                       RouteOptimizationService routeOptimizationService,
                                       String mapsApiKey) {
        this.geocodingService = geocodingService;
        this.routeOptimizationService = routeOptimizationService;
        this.mapsApiKey = mapsApiKey;
    }

    @PostMapping("/optimize")
    public ResponseEntity<ApiResponse<OptimizationResult>> optimizeRoutes(
            @Valid @RequestBody OptimizationRequest request) {

        logger.info("Received optimization request with {} deliveries", request.getDeliveryAddresses().size());

        try {
            // Geocode depot
            Location depot = geocodingService.geocodeAddress(request.getDepotAddress(), "Depot");
            if (depot == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Could not geocode depot address: " + request.getDepotAddress()));
            }

            // Geocode delivery locations
            List<Location> deliveryLocations = new ArrayList<>();
            for (int i = 0; i < request.getDeliveryAddresses().size(); i++) {
                String address = request.getDeliveryAddresses().get(i);
                String label = (request.getDeliveryLabels() != null && i < request.getDeliveryLabels().size())
                        ? request.getDeliveryLabels().get(i)
                        : "Stop " + (i + 1);

                Location location = geocodingService.geocodeAddress(address, label);
                if (location != null) {
                    deliveryLocations.add(location);
                } else {
                    logger.warn("Could not geocode address: {}", address);
                }
            }

            if (deliveryLocations.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("No valid delivery locations found"));
            }

            // Optimize routes
            OptimizationResult result = routeOptimizationService.optimizeRoutes(
                    depot, deliveryLocations, request.getConstraints()
            );

            return ResponseEntity.ok(
                    ApiResponse.success("Routes optimized successfully", result)
            );

        } catch (Exception e) {
            logger.error("Error during route optimization", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Optimization failed: " + e.getMessage()));
        }
    }

    @GetMapping("/config")
    public ResponseEntity<ApiResponse<String>> getConfig() {
        return ResponseEntity.ok(
                ApiResponse.success("API key retrieved", mapsApiKey)
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getStatus() {
        return ResponseEntity.ok(
                ApiResponse.success("Route optimization service is running", "OK")
        );
    }
}
