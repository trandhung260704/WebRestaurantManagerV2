package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UsersDTO {
    private Integer id_user;
    private String full_name;
    private String email;
    private String phone;
    private String birthday;
    private String gender;
    private String role;
    private Timestamp created_at;
}