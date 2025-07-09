package com.example.demo.controller;

import com.example.demo.dto.BillDTO;
import com.example.demo.dto.BillResponseDTO;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Discount;
import com.example.demo.entity.Orders;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.DiscountRepository;
import com.example.demo.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RequiredArgsConstructor
public class BillController {

    private final BillRepository billRepository;
    private final OrdersRepository ordersRepository;
    private final DiscountRepository discountRepository;

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    @GetMapping
    public List<BillResponseDTO> getAllBills() {
        return billRepository.findAll().stream().map(b -> {
            BillResponseDTO dto = new BillResponseDTO();
            dto.setId_bill(b.getId_bill());
            dto.setTotal_price(b.getTotal_price());
            dto.setPayment_method(b.getPayment_method());
            dto.setBill_time(b.getBill_time());
            dto.setId_discount(b.getDiscount() != null ? b.getDiscount().getId_discount() : null);

            BillResponseDTO.OrderDTO orderDTO = new BillResponseDTO.OrderDTO();
            orderDTO.setId_order(b.getOrder().getId_order());
            dto.setOrder(orderDTO);

            return dto;
        }).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Integer id) {
        Optional<Bill> bill = billRepository.findById(id);
        return bill.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<String> createBill(@RequestBody BillDTO dto) {
        Optional<Orders> order = ordersRepository.findById(dto.getId_order());
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().body("Order không tồn tại");
        }

        Discount discount = null;
        if (dto.getId_discount() != null) {
            Optional<Discount> discountOpt = discountRepository.findById(dto.getId_discount());
            if (discountOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Discount không tồn tại");
            }
            discount = discountOpt.get();
        }

        Bill bill = new Bill();
        bill.setOrder(order.get());
        bill.setDiscount(discount);
        bill.setTotal_price(dto.getTotal_price());
        bill.setPayment_method(dto.getPayment_method());

        billRepository.save(bill);
        return ResponseEntity.ok("Bill created successfully");
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    // DELETE: Xóa hóa đơn
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Integer id) {
        if (!billRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        billRepository.deleteById(id);
        return ResponseEntity.ok("Bill deleted successfully");
    }

    private BillDTO toDTO(Bill bill) {
        BillDTO dto = new BillDTO();
        dto.setId_bill(bill.getId_bill());
        dto.setId_order(bill.getOrder() != null ? bill.getOrder().getId_order() : null);
        dto.setId_discount(bill.getDiscount() != null ? bill.getDiscount().getId_discount() : null);
        dto.setTotal_price(bill.getTotal_price());
        dto.setPayment_method(bill.getPayment_method());
        dto.setBill_time(bill.getBill_time());
        return dto;
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(@PathVariable Integer id, @RequestBody BillDTO dto) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (optionalBill.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Orders> orderOpt = ordersRepository.findById(dto.getId_order());
        if (orderOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Order không tồn tại");
        }

        Discount discount = null;
        if (dto.getId_discount() != null) {
            Optional<Discount> discountOpt = discountRepository.findById(dto.getId_discount());
            if (discountOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Discount không tồn tại");
            }
            discount = discountOpt.get();
        }

        Bill bill = optionalBill.get();
        bill.setOrder(orderOpt.get());
        bill.setDiscount(discount);
        bill.setTotal_price(dto.getTotal_price());
        bill.setPayment_method(dto.getPayment_method());
        bill.setBill_time(dto.getBill_time());

        billRepository.save(bill);
        return ResponseEntity.ok("Bill updated successfully");
    }

}
