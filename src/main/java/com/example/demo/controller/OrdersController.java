package com.example.demo.controller;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UsersRepository usersRepository;
    private final FoodRepository foodRepository;
    private final BillRepository billRepository;
    private final FoodDetailRepository foodDetailRepository;
    private final IngredientRepository ingredientRepository;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO request) {
        Optional<Users> optionalUser = usersRepository.findById(request.getId_user());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại với ID: " + request.getId_user());
        }
        Users user = optionalUser.get();

        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Đơn hàng không có món ăn nào.");
        }

        BigDecimal total = BigDecimal.ZERO;

        Map<Integer, BigDecimal> ingredientRequiredMap = new HashMap<>();

        for (OrderItemDTO item : request.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Số lượng món phải lớn hơn 0.");
            }
            Optional<Food> optionalFood = foodRepository.findById(item.getId_food());
            if (optionalFood.isEmpty()) {
                return ResponseEntity.badRequest().body("Món ăn không tồn tại với ID: " + item.getId_food());
            }
            Food food = optionalFood.get();
            total = total.add(food.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            List<FoodDetail> details = foodDetailRepository.findByFood(food);
            for (FoodDetail detail : details) {
                Integer ingredientId = detail.getIngredient().getId_ingredient();
                BigDecimal usedQty = detail.getQuantity_used().multiply(BigDecimal.valueOf(item.getQuantity()));
                ingredientRequiredMap.put(ingredientId,
                        ingredientRequiredMap.getOrDefault(ingredientId, BigDecimal.ZERO).add(usedQty));
            }
        }
        for (Map.Entry<Integer, BigDecimal> entry : ingredientRequiredMap.entrySet()) {
            Integer ingId = entry.getKey();
            BigDecimal totalUsed = entry.getValue();

            Ingredient ingredient = ingredientRepository.findById(ingId).orElse(null);
            if (ingredient == null) {
                return ResponseEntity.badRequest().body("Nguyên liệu không tồn tại với ID: " + ingId);
            }

            if (ingredient.getQuantity().compareTo(totalUsed) < 0) {
                return ResponseEntity.badRequest().body("Không đủ nguyên liệu [" + ingredient.getName() + "] trong kho. Cần: " + totalUsed + " " + ingredient.getUnit() + ", còn: " + ingredient.getQuantity());
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : ingredientRequiredMap.entrySet()) {
            Ingredient ing = ingredientRepository.findById(entry.getKey()).get();
            ing.setQuantity(ing.getQuantity().subtract(entry.getValue()));
            ingredientRepository.save(ing);
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setOrder_time(new Timestamp(System.currentTimeMillis()));
//                order.setOrder_time(
//                request.getOrder_time() != null ?
//                        Timestamp.valueOf(request.getOrder_time()) :
//                        new Timestamp(System.currentTimeMillis())
//        );
        order.setStatus("PENDING");
        order.setTotal_price(total);
        Orders savedOrder = ordersRepository.save(order);

        for (OrderItemDTO item : request.getItems()) {
            Food food = foodRepository.findById(item.getId_food()).get();

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

        return ResponseEntity.ok("Đơn hàng và hóa đơn đã được tạo, kho nguyên liệu đã cập nhật.");
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "") String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> ordersPage;

        if (keyword.isBlank()) {
            ordersPage = ordersRepository.findAll(pageable);
        } else {
            ordersPage = ordersRepository.findByUser_UsernameContainingIgnoreCase(keyword, pageable);
        }
        return ResponseEntity.ok(ordersPage);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        Optional<Orders> optionalOrder = ordersRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Orders order = optionalOrder.get();
        order.setStatus(status);
        ordersRepository.save(order);

        return ResponseEntity.ok("Đã cập nhật trạng thái đơn hàng.");
    }
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        Optional<Orders> optionalOrder = ordersRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Orders order = optionalOrder.get();

        billRepository.deleteByOrder(order);

        orderDetailRepository.deleteByOrder(order);

        ordersRepository.delete(order);

        return ResponseEntity.ok("Đã xóa đơn hàng thành công.");
    }
//    @PreAuthorize("hasRole('MANAGER')")
//    @PutMapping("/{id}/status")
//    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id,
//                                               @RequestParam String status) {
//        return ordersRepository.findById(id)
//                .map(order -> {
//                    order.setStatus(status);
//                    ordersRepository.save(order);
//                    return ResponseEntity.ok("Cập nhật trạng thái thành công.");
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
}
