package com.example.demo.repository;

import com.example.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    Page<Orders> findByUser_UsernameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT SUM(o.total_price) FROM Orders o WHERE o.status = 'COMPLETED'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT HOUR(o.order_time) AS hour, COUNT(o) AS count " +
            "FROM Orders o GROUP BY HOUR(o.order_time) ORDER BY hour")
    Map<Integer, Long> countOrdersByHour();

    @Query("SELECT HOUR(o.order_time) AS hour, SUM(o.total_price) AS total " +
            "FROM Orders o GROUP BY HOUR(o.order_time) ORDER BY hour")
    Map<Integer, BigDecimal> calculateRevenueByHour();

//    @Query("SELECT new com.example.demo.dto.CustomerStatisticDTO(u.id_user, u.username, COUNT(o), SUM(o.total_price)) " +
//            "FROM Orders o JOIN o.user u " +
//            "GROUP BY u.id_user, u.username " +
//            "ORDER BY SUM(o.total_price) DESC")
//    Page<CustomerStatisticDTO> findTopCustomers(Pageable pageable);
}
