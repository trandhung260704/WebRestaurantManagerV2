package com.example.demo.controller;

import com.example.demo.dto.DiscountDTO;
import com.example.demo.entity.Discount;
import com.example.demo.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountRepository discountRepository;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public List<DiscountDTO> getAll() {
        return discountRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getById(@PathVariable Integer id) {
        return discountRepository.findById(id)
                .map(discount -> ResponseEntity.ok(toDTO(discount)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/code/{code}")
    public ResponseEntity<DiscountDTO> getByCode(@PathVariable String code) {
        return discountRepository.findByCode(code)
                .map(discount -> ResponseEntity.ok(toDTO(discount)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<String> createDiscount(@RequestBody DiscountDTO dto) {
        if (discountRepository.findByCode(dto.getCode()).isPresent()) {
            return ResponseEntity.badRequest().body("Code đã tồn tại.");
        }

        Discount discount = new Discount();
        copyFromDTO(dto, discount);
        discountRepository.save(discount);
        return ResponseEntity.ok("Tạo mã giảm giá thành công.");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDiscount(@PathVariable Integer id, @RequestBody DiscountDTO dto) {
        Optional<Discount> existing = discountRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Discount discount = existing.get();
        copyFromDTO(dto, discount);
        discountRepository.save(discount);
        return ResponseEntity.ok("Cập nhật thành công.");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscount(@PathVariable Integer id) {
        if (!discountRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        discountRepository.deleteById(id);
        return ResponseEntity.ok("Xoá mã giảm giá thành công.");
    }

    private DiscountDTO toDTO(Discount d) {
        DiscountDTO dto = new DiscountDTO();
        dto.setId_discount(d.getId_discount());
        dto.setCode(d.getCode());
        dto.setName(d.getName());
        dto.setStart_date(d.getStart_date());
        dto.setEnd_date(d.getEnd_date());
        dto.setDiscount_percent(d.getDiscount_percent());
        return dto;
    }

    private void copyFromDTO(DiscountDTO dto, Discount d) {
        d.setCode(dto.getCode());
        d.setName(dto.getName());
        d.setStart_date(dto.getStart_date());
        d.setEnd_date(dto.getEnd_date());
        d.setDiscount_percent(dto.getDiscount_percent());
    }
}
