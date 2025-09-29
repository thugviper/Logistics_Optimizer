package com.major_project.Logistics_Optimiser.Controller;

import com.major_project.Logistics_Optimiser.Service.Maps;
import com.major_project.Logistics_Optimiser.Model.Location;
import com.major_project.Logistics_Optimiser.Dto.AddLocationRequest;
import com.major_project.Logistics_Optimiser.Model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin(origins = "*")
public class MapsController {
    private static final Logger logger = LoggerFactory.getLogger(MapsController.class);
    private final Maps maps;
    private final String jsApiKey;

    public MapsController(Maps mapsService, String jsApiKey) {
        this.maps = mapsService;
        this.jsApiKey = jsApiKey;
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus() {
        Map<String, Object> status = Map.of(
                "service", "Google Maps Integration",
                "status", "running",
                "locationCount", maps.getLocationCount(),
                "hasJsApiKey", !jsApiKey.isEmpty()
        );
        return ResponseEntity.ok(ApiResponse.success("Service is running", status));
    }

    @PostMapping("/locations")
    public ResponseEntity<ApiResponse<Location>> addLocation(
            @Valid @RequestBody AddLocationRequest request,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid request: " + result.getAllErrors().getFirst().getDefaultMessage()));
        }

        logger.info("Adding location: {} with label: {}", request.getAddress(), request.getLabel());

        Location location = maps.addLocation(request);
        if (location != null) {
            return ResponseEntity.ok(
                    ApiResponse.success("Location added successfully", location)
            );
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Could not geocode address: " + request.getAddress()));
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<Location>>> getAllLocations() {
        List<Location> locations = maps.getAllLocations();
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved all locations", locations)
        );
    }

    @GetMapping("/locations/search")
    public ResponseEntity<ApiResponse<List<Location>>> searchLocations(
            @RequestParam String query) {
        List<Location> results = maps.searchLocations(query);
        return ResponseEntity.ok(
                ApiResponse.success("Search completed", results)
        );
    }

    @DeleteMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> clearAllLocations() {
        maps.clearLocations();
        logger.info("All locations cleared");
        return ResponseEntity.ok(
                ApiResponse.success("All locations cleared", null)
        );
    }

    @DeleteMapping("/locations/{index}")
    public ResponseEntity<ApiResponse<Void>> removeLocation(@PathVariable int index) {
        boolean removed = maps.removeLocation(index);
        if (removed) {
            logger.info("Location at index {} removed", index);
            return ResponseEntity.ok(
                    ApiResponse.success("Location removed", null)
            );
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid location index: " + index));
        }
    }

    @GetMapping("/config")
    public ResponseEntity<ApiResponse<Map<String, String>>> getConfig() {
        Map<String, String> config = Map.of(
                "jsApiKey", jsApiKey
        );
        return ResponseEntity.ok(
                ApiResponse.success("Configuration retrieved", config)
        );
    }

    // Simple endpoint to test the service
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Google Maps Service is working! Current locations: " + maps.getLocationCount());
    }
}
