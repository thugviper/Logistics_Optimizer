package com.major_project.Logistics_Optimiser.Service;

import com.major_project.Logistics_Optimiser.Model.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class Geocoding {
    private static final Logger logger = LoggerFactory.getLogger(Geocoding.class);
    private final GeoApiContext geoApiContext;

    public Geocoding(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    public Location geocodeAddress(String address, String label) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, address).await();

            if (results.length > 0) {
                double lat = results[0].geometry.location.lat;
                double lng = results[0].geometry.location.lng;
                String formattedAddress = results[0].formattedAddress;

                logger.info("Geocoded '{}' to: {}, {}", address, lat, lng);
                return new Location(lat, lng, formattedAddress, label);
            } else {
                logger.warn("No results found for address: {}", address);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error geocoding address '{}': {}", address, e.getMessage());
            return null;
        }
    }
}
