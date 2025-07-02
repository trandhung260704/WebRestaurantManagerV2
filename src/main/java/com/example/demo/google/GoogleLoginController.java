package com.example.demo.google;

import com.example.demo.entity.Users;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class GoogleLoginController {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, String> body) {
        String token = body.get("token");

        try {
            Users googleUser = LoginGoogle.verifyIdToken(token);
            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("Thiếu token");
            }
            Users user = usersRepository.findByEmail(googleUser.getEmail()).orElseGet(() -> {
                googleUser.setRole("CUSTOMER");
                return usersRepository.save(googleUser);
            });

            Map<String, Object> claims = Map.of(
                    "id", user.getId_user(),
                    "email", user.getEmail(),
                    "role", user.getRole()
            );
            String jwt = jwtUtil.generateToken(claims, String.valueOf(user.getId_user()));

            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "full_name", user.getFull_name(),
                    "role", user.getRole()
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token xác minh thất bại");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
