package com.asep.searchservice.controller;

import com.asep.searchservice.document.UmkmDocument;
import com.asep.searchservice.repository.UmkmSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SearchController {

    private final UmkmSearchRepository searchRepository;

    // 1. Search bar: "bakso", "sate"
    @GetMapping
    public List<UmkmDocument> search(@RequestParam String q) {
        System.out.println("search q");
        return searchRepository.findByUmkmNameContainingOrProductNameContaining(q, q);
    }

    // 2. Filter by kecamatan
    @GetMapping("/kecamatan/{kecamatan}")
    public List<UmkmDocument> byKecamatan(@PathVariable String kecamatan) {
        System.out.println("search kecamatan");
        return searchRepository.findByKecamatan(kecamatan);
    }

    // 3. Search terdekat: /api/search/nearby?lat=-6.2&lon=106.8
    @GetMapping("/nearby")
    public List<UmkmDocument> nearby(@RequestParam Double lat, @RequestParam Double lon) {
        System.out.println("nearby");
        return searchRepository.findByLocationNear(lat, lon);
    }
}