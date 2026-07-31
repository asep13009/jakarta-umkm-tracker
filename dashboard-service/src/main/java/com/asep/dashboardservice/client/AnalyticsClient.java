package com.asep.dashboardservice.client;

import com.asep.dashboardservice.dto.SummaryDto;
import com.asep.dashboardservice.dto.TopUmkmDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Service
@RequiredArgsConstructor
public class AnalyticsClient {

    private final RestTemplate restTemplate;

    @Value("${analytics.service.url}")
    private String analyticsUrl;

    public List<TopUmkmDto> getTopUmkm() {
        System.out.println("getTopUmkm");

        String url = analyticsUrl + "/api/analytics/top-umkm";
        ResponseEntity<List<TopUmkmDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TopUmkmDto>>() {}
        );
        return response.getBody();

    }


    public SummaryDto getSummary() {
        System.out.println("getSummary");

        String url = analyticsUrl + "/api/analytics/summary";
        return restTemplate.getForObject(url, SummaryDto.class);
    }





}