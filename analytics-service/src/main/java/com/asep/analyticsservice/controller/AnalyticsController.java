package com.asep.analyticsservice.controller;

import com.asep.analyticsservice.dto.SummaryDto;
import com.asep.analyticsservice.dto.TopUmkmDto;
import com.asep.analyticsservice.dto.Trend24hDto;
import com.asep.analyticsservice.repository.OrderRepository;
import com.asep.analyticsservice.service.AnalyticsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;
/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*") // biar React bisa akses
public class AnalyticsController {

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private final AnalyticsService analyticsService;

    public AnalyticsController(OrderRepository orderRepository, RedisTemplate<String, String> redisTemplate, AnalyticsService analyticsService) {
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/top-umkm/{kecamatan}")
    public Map<String, Object> getTopUmkm(@PathVariable String kecamatan) {
        System.out.println( "hit /top-umkm/{kecamatan}");
        return Map.of(
                "source", "postgres",
                "data", orderRepository.findTopUmkmByKecamatan(kecamatan)
        );
    }

    @GetMapping("/realtime-sales/{kecamatan}")
    public Map<String, Object> getRealtimeSales(@PathVariable String kecamatan) {
        System.out.println( "hit realtime-sales/{kecamatan}");
        String key = "top:sales:" + kecamatan;
        String sales = redisTemplate.opsForValue().get(key);

        if (sales == null) {
            redisTemplate.opsForValue().set(key, "0", Duration.ofMinutes(1));
            sales = "0";
        }

        return Map.of(
                "source", "redis",
                "kecamatan", kecamatan,
                "salesLastMinute", sales
        );
    }

    @GetMapping("/trend/{kecamatan}")
    public Map<String, Object> getTrend(@PathVariable String kecamatan) {
        System.out.println( "hit /trend/{kecamatan}");
        return Map.of(
                "data", orderRepository.findHourlyTrend(kecamatan)
        );
    }


    @GetMapping("/top-umkm")
    public List<TopUmkmDto> getTopUmkmDefault() {
        System.out.println( "hit /top-umkm");
        return  orderRepository.findTopUmkm();
    }


    @GetMapping("/summary")
    public SummaryDto getSummary() {
        System.out.println( "hit /summary");
        return analyticsService.getSummaryToday();
    }


    @GetMapping("/trend-24h/{kecamatan}")
    public List<Trend24hDto> getTrend24h(@PathVariable String kecamatan) {
        System.out.println( "hit /trend24");
        return analyticsService.getTrend24h(kecamatan);
    }


}