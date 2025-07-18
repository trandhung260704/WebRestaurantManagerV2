package com.example.demo.repository;

import com.example.demo.entity.RestaurantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantInfoRepository extends JpaRepository<RestaurantInfo, Long> {
    Optional<RestaurantInfo> findFirstByOrderById();
}