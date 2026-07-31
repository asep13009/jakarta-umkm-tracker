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
@Table(name = "umkm")
public class Umkm {

    @Id
    private Long id;

    private String name; // "Bakso Pak Jono"

//    @Column(nullable = false)
//    private String ownerName; // "Jono"

    private String kecamatan; // "Jakarta Selatan"

//    @Column(nullable = false)
//    private String category; // "Makanan", "Kerajinan", "Jasa"

//    private String address;
//    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}