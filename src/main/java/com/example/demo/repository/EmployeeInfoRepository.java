package com.example.demo.repository;

import com.example.demo.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, Integer> {

}
