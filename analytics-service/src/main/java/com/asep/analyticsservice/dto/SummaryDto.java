package com.asep.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */

@Data
@NoArgsConstructor
public class SummaryDto {
    private Long totalSalesToday;
    private Long totalOrdersToday;
    private Long avgOrderValue;


    public SummaryDto(Long totalSales, Long totalOrders, Long avgOrderValue) {
        this.totalSalesToday=totalSales;
        this.totalOrdersToday = totalOrders;
        this.avgOrderValue = avgOrderValue;
    }
}
