package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "history_login")
@Getter
@Setter
@NoArgsConstructor
public class HistoryLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = true)
    private Users user;

    @Column(name = "login_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loginTime;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(length = 20, nullable = false)
    private String status; // "SUCCESS" hoặc "FAILED"

    public HistoryLogin(Users user, String ipAddress, String userAgent, String status) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.status = status;
        this.loginTime = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (loginTime == null) {
            loginTime = LocalDateTime.now();
        }
    }
}