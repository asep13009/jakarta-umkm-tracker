package com.asep.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 31/7/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trend24hDto {
    private LocalDateTime hour;
    private BigDecimal total;
}
