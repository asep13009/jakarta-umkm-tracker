package com.asep.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long umkmId;
    private String productName;
    private Long  amount;
    private LocalDateTime orderTime;
}