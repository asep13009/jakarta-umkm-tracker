package com.asep.analyticsservice.repository;

import com.asep.analyticsservice.dto.TopUmkmDto;
import com.asep.analyticsservice.dto.Trend24hDto;
import com.asep.analyticsservice.entity.Order;
import com.asep.analyticsservice.entity.Umkm;
import com.asep.analyticsservice.repository.projection.HourlySalesProjection;
import com.asep.analyticsservice.repository.projection.TopUmkmProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
@Repository
public interface UmkmRepository extends JpaRepository<Umkm, Long> {

}
