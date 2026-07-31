package com.asep.dashboardservice.service;

import com.asep.dashboardservice.dto.RecentOrderDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final RedisTemplate<String, String> redisTemplate;
    public Map<String, Long> getRealtimeMap() {
        Map<String, Long> result = new HashMap<>();
        List<String> kecamatans = List.of("Jakarta Pusat", "Jakarta Selatan", "Jakarta Barat", "Jakarta Timur", "Jakarta Utara");
        for (String kec : kecamatans) {
            String val = redisTemplate.opsForValue().get("top:sales:" + kec);
            result.put(kec, val != null ? Long.parseLong(val) : 0L);
        }
        return result;
    }

    public List<RecentOrderDto> getRecentOrders() {
        // ambil 10 data terakhir dari redis list "recent:orders"
        System.out.println("getRO");
        List<String> orders = redisTemplate.opsForList().range("recent:orders", 0, 9);
        return orders.stream().map(json -> new Gson().fromJson(json, RecentOrderDto.class)).toList();
    }

}