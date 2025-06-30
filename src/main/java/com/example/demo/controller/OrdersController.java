package com.example.demo.controller;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderRequestDTO;
import com.example.demo.entity.Food;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Users;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository ordersDetailRepository;
    private final UsersRepository usersRepository;
    private final FoodRepository foodRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO request) {
        Users user = usersRepository.findById(request.getId_user()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User không tồn tại");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setOrder_time(new Timestamp(System.currentTimeMillis()));
        order.setStatus("chưa phục vụ");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO item : request.getItems()) {
            Food food = foodRepository.findById(item.getId_food()).orElse(null);
            if (food == null) {
                return ResponseEntity.badRequest().body("Món ăn không tồn tại với id: " + item.getId_food());
            }

            BigDecimal itemTotal = food.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotal_price(total);
        Orders savedOrder = ordersRepository.save(order);

        for (OrderItemDTO item : request.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setFood(foodRepository.findById(item.getId_food()).orElse(null));
            detail.setQuantity(item.getQuantity());
            ordersDetailRepository.save(detail);
        }

        return ResponseEntity.ok("Đơn hàng đã được tạo thành công!");
    }
}

