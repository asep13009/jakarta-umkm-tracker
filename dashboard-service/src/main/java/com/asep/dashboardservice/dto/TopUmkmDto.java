package com.asep.dashboardservice.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Data
public class TopUmkmDto {

    private String umkmName;
    private String category;
    private Long totalSales;
    private Long totalOrders;

}
