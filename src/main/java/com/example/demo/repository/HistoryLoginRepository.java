package com.example.demo.repository;

import com.example.demo.entity.HistoryLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryLoginRepository extends JpaRepository<HistoryLogin, Long> {
}