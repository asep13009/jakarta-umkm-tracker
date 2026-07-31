package com.asep.searchservice.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDateTime;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Data
@Builder
@Document(indexName = "umkm")
public class UmkmDocument {

    @Id
    private String umkmId;

    private String umkmName;

    private String productName;
    private String kecamatan;

    private GeoPoint location; // buat search radius

    private Long totalSales;

    private String lastOrderAt;
}