package com.example.demo.repository;

import com.example.demo.entity.Food;
import com.example.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Page<Orders> findByUser_UsernameContainingIgnoreCase(String name, Pageable pageable);
}
