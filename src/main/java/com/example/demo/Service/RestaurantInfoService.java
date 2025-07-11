package com.example.demo.Service;

import com.example.demo.entity.RestaurantInfo;
import com.example.demo.repository.RestaurantInfoRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RestaurantInfoService {

    @Autowired
    private RestaurantInfoRepository restaurantInfoRepository;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    public RestaurantInfo getRestaurantInfo() {
        return restaurantInfoRepository.findFirstByOrderById()
                .orElse(getDefaultRestaurantInfo());
    }

    public RestaurantInfo updateRestaurantInfo(RestaurantInfo restaurantInfo) {
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(googleMapsApiKey)
                    .build();

            GeocodingResult[] results = GeocodingApi.geocode(context, restaurantInfo.getAddress()).await();

            if (results.length > 0) {
                LatLng location = results[0].geometry.location;
                restaurantInfo.setLatitude(location.lat);
                restaurantInfo.setLongitude(location.lng);
            }
        } catch (Exception e) {
            System.err.println("Error geocoding address: " + e.getMessage());
        }

        return restaurantInfoRepository.save(restaurantInfo);
    }

    private RestaurantInfo getDefaultRestaurantInfo() {
        RestaurantInfo info = new RestaurantInfo();
        info.setName("Restaurant Eternity");
        info.setAddress("1 Võ Văn Ngân, Thủ Đức, TP.HCM");
        info.setPhone("0938 196 822");
        info.setEmail("trandhungeternity@restaurant.com");
        info.setFacebook("Restaurant Eternity");
        info.setOpeningHours("7:00 - 22:00 (Thứ 2 - Chủ nhật)");
        info.setLatitude(10.8231);
        info.setLongitude(106.6297);
        return info;
    }
}
