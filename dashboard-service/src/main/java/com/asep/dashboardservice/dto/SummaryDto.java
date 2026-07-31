package com.asep.dashboardservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */

@Data
public class SummaryDto {
    private Long totalSalesToday;
    private Long totalOrdersToday;
    private Long avgOrderValue;


}
