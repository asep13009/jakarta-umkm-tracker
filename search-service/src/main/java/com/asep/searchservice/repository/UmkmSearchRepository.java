package com.asep.searchservice.repository;

import com.asep.searchservice.document.UmkmDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Repository
public interface UmkmSearchRepository extends ElasticsearchRepository<UmkmDocument, String> {

    // Search by nama UMKM atau Produk
    List<UmkmDocument> findByUmkmNameContainingOrProductNameContaining(String umkmName, String productName);

    // Search by kecamatan
    List<UmkmDocument> findByKecamatan(String kecamatan);

    // Search radius 5km dari titik
    @Query("{\"bool\": {\"must\": {\"match_all\": {}}, \"filter\": {\"geo_distance\": {\"distance\": \"5km\", \"location\": {\"lat\": ?0, \"lon\": ?1} }}}}")
    List<UmkmDocument> findByLocationNear(Double lat, Double lon);
}