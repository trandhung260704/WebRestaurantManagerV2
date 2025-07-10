package com.example.demo.controller;

import com.example.demo.Service.RestaurantInfoService;
import com.example.demo.entity.RestaurantInfo;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurant")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
public class RestaurantInfoController {

    @Autowired
    private RestaurantInfoService restaurantInfoService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping("/info")
    public ResponseEntity<RestaurantInfo> getRestaurantInfo() {
        RestaurantInfo info = restaurantInfoService.getRestaurantInfo();
        return ResponseEntity.ok(info);
    }

    @PutMapping("/info")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RestaurantInfo> updateRestaurantInfo(@RequestBody RestaurantInfo restaurantInfo) {
        RestaurantInfo updatedInfo = restaurantInfoService.updateRestaurantInfo(restaurantInfo);
        return ResponseEntity.ok(updatedInfo);
    }

    @GetMapping("/geocode")
    public ResponseEntity<Map<String, Object>> geocodeAddress(@RequestParam String address) {
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(googleMapsApiKey)
                    .build();

            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

            Map<String, Object> response = new HashMap<>();
            if (results.length > 0) {
                LatLng location = results[0].geometry.location;
                response.put("latitude", location.lat);
                response.put("longitude", location.lng);
                response.put("formattedAddress", results[0].formattedAddress);
                response.put("success", true);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy địa chỉ");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi khi geocode địa chỉ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
