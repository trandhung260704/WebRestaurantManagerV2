package com.example.demo.repository;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    void deleteByOrder(Orders order);
}
