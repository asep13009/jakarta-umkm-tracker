package com.asep.searchservice.dto;

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
public class OrderEvent {
    private String orderId;
    private String umkmId;
    private String umkmName;
    private String productName;
    private String kecamatan;
    private Double latitude;
    private Double longitude;
    private Long amount;
    private LocalDateTime timestamp;
}
