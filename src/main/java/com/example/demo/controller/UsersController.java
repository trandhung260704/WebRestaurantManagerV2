package com.example.demo.controller;

import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UsersController {

    private final UsersRepository usersRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String phone = body.get("phone");

            if (email == null || phone == null || body.get("password") == null || body.get("full_name") == null) {
                return ResponseEntity.badRequest().body("Thiếu thông tin bắt buộc");
            }

            if (usersRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã tồn tại");
            }

            if (usersRepository.findByPhone(phone).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã tồn tại");
            }

            Users user = new Users();
            user.setFull_name(body.get("full_name"));
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(body.get("password"));
            user.setGender(body.getOrDefault("gender", null));

            String birthdayStr = body.get("birthday");
            Date birthday = null;
            if (birthdayStr != null && !birthdayStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                birthday = sdf.parse(birthdayStr);
            }
            user.setBirthday(birthday);

            user.setRole("CUSTOMER");
            user.setCreated_at(new Timestamp(System.currentTimeMillis()));

            usersRepository.save(user);
            return ResponseEntity.ok("Đăng ký thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi máy chủ: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER') or hasRole('MANAGER')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Users user)) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        UsersDTO dto = new UsersDTO();
        dto.setId_user(user.getId_user());
        dto.setFull_name(user.getFull_name());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole());
        dto.setCreated_at(user.getCreated_at());

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }
}