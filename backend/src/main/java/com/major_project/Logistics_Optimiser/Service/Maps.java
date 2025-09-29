package com.major_project.Logistics_Optimiser.Service;

import com.major_project.Logistics_Optimiser.Model.Location;
import com.major_project.Logistics_Optimiser.Dto.AddLocationRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Maps {
    private final Geocoding geocoding;
    private final List<Location> locations = new ArrayList<>();

    public Maps(Geocoding geocodingService) {
        this.geocoding = geocodingService;
    }

    public Location addLocation(AddLocationRequest request) {
        Location point = geocoding.geocodeAddress(
                request.getAddress(),
                request.getLabel()
        );

        if (point != null) {
            locations.add(point);
        }
        return point;
    }

    public List<Location> getAllLocations() {
        return new ArrayList<>(locations);
    }

    public void clearLocations() {
        locations.clear();
    }

    public int getLocationCount() {
        return locations.size();
    }

    public List<Location> searchLocations(String query) {
        return locations.stream()
                .filter(loc -> loc.getAddress().toLowerCase().contains(query.toLowerCase()) ||
                        (loc.getLabel() != null && loc.getLabel().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
    }

    public boolean removeLocation(int index) {
        if (index >= 0 && index < locations.size()) {
            locations.remove(index);
            return true;
        }
        return false;
    }
}
