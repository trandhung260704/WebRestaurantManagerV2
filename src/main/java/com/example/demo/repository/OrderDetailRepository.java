package com.example.demo.repository;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    void deleteByOrder(Orders order);

    @Query("SELECT od.food.name, SUM(od.quantity) as totalSold " +
            "FROM OrderDetail od " +
            "GROUP BY od.food.name " +
            "ORDER BY totalSold DESC LIMIT 5")
    List<Object[]> findTop5BestSellingFoods();
}
