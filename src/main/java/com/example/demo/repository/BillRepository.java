package com.example.demo.repository;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    void deleteByOrder(Orders order);

    @Query("SELECT SUM(b.total_price) FROM Bill b")
    BigDecimal getTotalRevenue();
}