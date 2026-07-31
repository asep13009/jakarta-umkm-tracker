package com.asep.searchservice.consumer;

import com.asep.searchservice.document.UmkmDocument;
import com.asep.searchservice.dto.OrderEvent;
import com.asep.searchservice.repository.UmkmSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final UmkmSearchRepository searchRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "umkm-orders", groupId = "search-group")
    public void consume(String message) { // Receive as String
        try {
            // Parse JSON manually
            JsonNode json = objectMapper.readTree(message);

            long umkmId = json.get("umkmId").asLong();
            String stringId = String.valueOf(umkmId);
            String umkmName = json.get("umkmName").asText();
            String kecamatan = json.get("kecamatan").asText();
            long amount = json.get("amount").asLong();
            String timestamp = json.get("timestamp").asText();

            // Fetch or create Document
            UmkmDocument doc = searchRepository.findById(stringId)
                    .orElse(UmkmDocument.builder()
                            .umkmId(stringId)
                            .umkmName(umkmName)
                            .totalSales(0L)
                            .build());

            // Safe Check for fields that might be missing in your generator JSON
            doc.setProductName(json.has("productName") ? json.get("productName").asText() : "Unknown");
            doc.setKecamatan(kecamatan);

            // Safe fallback for coordinates if your generator doesn't provide them yet
            double lat = json.has("latitude") ? json.get("latitude").asDouble() : -6.2000;
            double lon = json.has("longitude") ? json.get("longitude").asDouble() : 106.8166;
            doc.setLocation(new GeoPoint(lat, lon));

            doc.setTotalSales(doc.getTotalSales() + amount);
            doc.setLastOrderAt(timestamp);

//            System.out.println("save umkm-orders -> elastic :: " + doc);
            searchRepository.save(doc);

        } catch (Exception e) {
            System.err.println("Failed to process Elasticsearch indexing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}