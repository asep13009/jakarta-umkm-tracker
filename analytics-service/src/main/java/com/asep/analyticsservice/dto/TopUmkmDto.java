package com.asep.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopUmkmDto {

    private String umkmName;
//    private String category;
    private BigDecimal totalSales;
    private Long totalOrders;

}
