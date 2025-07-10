package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "sender_role")
    private String senderRole;

    @Column(name = "receiver_id")
    private Integer receiverId;

    @Column(name = "receiver_role")
    private String receiverRole;

    private String content;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
