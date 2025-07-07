package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhone(String phone);

    Users findByEmailOrPhone(String email, String phone);

    @Query("SELECT AVG(YEAR(CURRENT_DATE) - YEAR(u.birthday)) FROM Users u WHERE u.role = 'CUSTOMER' AND u.birthday IS NOT NULL")
    Double findAverageCustomerAge();

    @Query("SELECT (YEAR(CURRENT_DATE) - YEAR(u.birthday)) AS age, COUNT(u) " +
            "FROM Users u WHERE u.role = 'CUSTOMER' AND u.birthday IS NOT NULL " +
            "GROUP BY age ORDER BY age")
    List<Object[]> findCustomerAgeCounts();
}
