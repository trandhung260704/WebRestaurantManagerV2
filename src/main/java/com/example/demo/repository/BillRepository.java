package com.example.demo.repository;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    void deleteByOrder(Orders order);
}