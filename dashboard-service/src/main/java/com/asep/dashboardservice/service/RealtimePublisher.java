package com.asep.dashboardservice.service;

import com.asep.dashboardservice.client.AnalyticsClient;
import com.asep.dashboardservice.dto.RecentOrderDto;
import com.asep.dashboardservice.dto.SummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RealtimePublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final DashboardService dashboardService;
    private final AnalyticsClient analyticsClient;

    @Scheduled(fixedRate = 3000) // tiap 3 detik
    public void pushRealtimeData() {
        Map<String, Long> mapData = dashboardService.getRealtimeMap(); // name (kecamatan) , sales
        SummaryDto summary = analyticsClient.getSummary();
        List<RecentOrderDto> recentOrderDto =  dashboardService.getRecentOrders();


        System.out.println("masuk ga nih >" +mapData);
        System.out.println("masuk ga summary >" +summary);
        System.out.println("masuk ga recentOrderDto >" +recentOrderDto);
        messagingTemplate.convertAndSend("/topic/realtime-map", mapData);
        messagingTemplate.convertAndSend("/topic/summary", summary);
        messagingTemplate.convertAndSend("/topic/recent-orders", recentOrderDto);
    }
}