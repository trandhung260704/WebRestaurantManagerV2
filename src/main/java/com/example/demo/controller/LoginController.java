package com.example.demo.controller;

import com.example.demo.entity.HistoryLogin;
import com.example.demo.entity.Users;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.HistoryLoginRepository;
import com.example.demo.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RequiredArgsConstructor
public class LoginController {

    private final UsersRepository usersRepo;
    private final JwtUtil jwtUtil;
    private final HistoryLoginRepository historyLoginRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Users user = usersRepo.findByEmailOrPhone(username, username);

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        HistoryLogin history = new HistoryLogin();
        history.setLoginTime(LocalDateTime.now());
        history.setIpAddress(ip);
        history.setUserAgent(userAgent);

        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            history.setUser(user);
            history.setStatus("SUCCESS");
            historyLoginRepo.save(history);

            Map<String, Object> claims = Map.of(
                    "id", user.getId_user(),
                    "email", user.getEmail(),
                    "role", user.getRole()
            );
            String token = jwtUtil.generateToken(claims, String.valueOf(user.getId_user()));

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "full_name", user.getFull_name(),
                    "role", user.getRole()
            ));
        } else {
            if (user != null) {
                history.setUser(user);
            }
            history.setStatus("FAILED");
            historyLoginRepo.save(history);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Thông tin đăng nhập không chính xác");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Users user)) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }
        return ResponseEntity.ok(Map.of(
                "id", user.getId_user(),
                "full_name", user.getFull_name(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}