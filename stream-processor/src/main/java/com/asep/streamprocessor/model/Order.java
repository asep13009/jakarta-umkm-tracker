package com.asep.streamprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String umkmName;
    private Long amount;
    private String kecamatan;
    private String timestamp;
}