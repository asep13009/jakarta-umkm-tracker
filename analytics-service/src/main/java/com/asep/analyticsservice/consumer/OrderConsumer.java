package com.asep.analyticsservice.consumer;

import com.asep.analyticsservice.entity.Order;
import com.asep.analyticsservice.entity.Umkm;
import com.asep.analyticsservice.repository.OrderRepository;
import com.asep.analyticsservice.repository.UmkmRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final UmkmRepository umkmRepository;

    @KafkaListener(topics = "umkm-orders", groupId = "analytics-group")
    public void consume(String message) {
//        System.out.println("message :: "+message);


        try {
            JsonNode json = objectMapper.readTree(message);
            Umkm umkm = new Umkm();
            umkm.setId(json.get("umkmId").asLong());
            umkm.setName(json.get("umkmName").asText());
            umkm.setKecamatan(json.get("kecamatan").asText());
            umkmRepository.saveAndFlush(umkm);

            Order order = new Order();
            order.setUmkmId(json.get("umkmId").asLong());
            order.setAmount(json.get("amount").asLong());
//            order.setProductName(json.get("productname").asString());
            order.setOrderTime(LocalDateTime.parse(json.get("timestamp").asText()));

            orderRepository.save(order);
        } catch (Exception e) {
            System.out.println("error UMKM ORDER send to db  "+e.getMessage());
        }

    }
}