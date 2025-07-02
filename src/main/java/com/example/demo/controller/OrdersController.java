package com.example.demo.controller;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UsersRepository usersRepository;
    private final FoodRepository foodRepository;
    private final BillRepository billRepository;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO request) {
        Optional<Users> optionalUser = usersRepository.findById(request.getId_user());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại với ID: " + request.getId_user());
        }
        Users user = optionalUser.get();

        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Đơn hàng không có món ăn nào.");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setOrder_time(new Timestamp(System.currentTimeMillis()));
        order.setStatus("PENDING");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO item : request.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Số lượng món phải lớn hơn 0.");
            }

            Optional<Food> optionalFood = foodRepository.findById(item.getId_food());
            if (optionalFood.isEmpty()) {
                return ResponseEntity.badRequest().body("Món ăn không tồn tại với ID: " + item.getId_food());
            }

            Food food = optionalFood.get();
            BigDecimal itemTotal = food.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotal_price(total);
        Orders savedOrder = ordersRepository.save(order);

        for (OrderItemDTO item : request.getItems()) {
            Food food = foodRepository.findById(item.getId_food()).orElse(null);
            if (food == null) continue;

            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setFood(food);
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }

        Bill bill = new Bill();
        bill.setOrder(savedOrder);
        bill.setTotal_price(total);
        bill.setPayment_method("CASH");
        bill.setDiscount(null);
        bill.setBill_time(new Timestamp(System.currentTimeMillis()));
        billRepository.save(bill);

        return ResponseEntity.ok("Đơn hàng và hóa đơn đã được tạo thành công!");
    }
}
