package com.asep.analyticsservice.service;

import com.asep.analyticsservice.dto.SummaryDto;
import com.asep.analyticsservice.dto.Trend24hDto;
import com.asep.analyticsservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 31/7/2026
 */
@Service
public class AnalyticsService {

     @Autowired
      OrderRepository orderRepository;


    public SummaryDto getSummaryToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Long totalSales = orderRepository.sumAmountByTimestampAfter(startOfDay);
        Long totalOrders = orderRepository.countByOrderTimeAfter(startOfDay);
        return new SummaryDto(totalSales, totalOrders, totalSales / totalOrders);
    }

    public List<Trend24hDto> getTrend24h(String kecamatan) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return orderRepository.findTrend24hRaw(startOfDay, kecamatan);
    }

}
