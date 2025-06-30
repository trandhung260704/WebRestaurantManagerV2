package com.example.demo.repository;

import com.example.demo.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    Optional<Discount> findByCode(String code);
}
