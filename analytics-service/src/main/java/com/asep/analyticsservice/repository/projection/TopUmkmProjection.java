package com.asep.analyticsservice.repository.projection;

import lombok.Data;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */

public interface TopUmkmProjection {
    String getUmkmName();
    String getCategory();
    Long getTotalSales();
    Long getTotalOrders();
}
