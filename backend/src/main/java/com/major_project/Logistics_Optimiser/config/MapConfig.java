package com.major_project.Logistics_Optimiser.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfig {
    @Value("${geocoding.api.key}")
    private String geocodingApiKey;

    @Value("${js.api.key}")
    private String jsApiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(geocodingApiKey)
                .build();
    }

    @Bean
    public String jsApiKey() {
        return jsApiKey;
    }
}
