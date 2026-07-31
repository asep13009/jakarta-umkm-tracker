package com.asep.dashboardservice.controller;

import com.asep.dashboardservice.client.AnalyticsClient;
import com.asep.dashboardservice.dto.RecentOrderDto;
import com.asep.dashboardservice.dto.SummaryDto;
import com.asep.dashboardservice.dto.TopUmkmDto;
import com.asep.dashboardservice.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AnalyticsClient analyticsClient;

    @GetMapping("/summary")
    public SummaryDto getSummary() {
        return analyticsClient.getSummary();
    }

    @GetMapping("/top-umkm")
    public List<TopUmkmDto> getTopUmkm() {
        return analyticsClient.getTopUmkm();
    }

    @GetMapping("/recent-orders")
    public List<RecentOrderDto> getRecentOrders() {
        return dashboardService.getRecentOrders(); // ambil dari redis list
    }

}