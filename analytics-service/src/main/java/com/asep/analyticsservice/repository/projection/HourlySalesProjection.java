package com.asep.analyticsservice.repository.projection;

import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
public interface HourlySalesProjection {
    LocalDateTime getHour();         // dari DATE_TRUNC('hour', order_time)
    Long getHourlySales();           // dari SUM(amount)
    Long getRunningTotal();          // dari SUM(SUM) OVER
}
