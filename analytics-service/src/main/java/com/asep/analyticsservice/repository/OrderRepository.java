package com.asep.analyticsservice.repository;

import com.asep.analyticsservice.dto.TopUmkmDto;
import com.asep.analyticsservice.dto.Trend24hDto;
import com.asep.analyticsservice.entity.Order;
import com.asep.analyticsservice.repository.projection.HourlySalesProjection;
import com.asep.analyticsservice.repository.projection.TopUmkmProjection;
import org.springframework.data.domain.PageRequest;
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
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Query 1: Top 5 UMKM terlaris di 1 kecamatan 24 jam terakhir
    @Query(value = """
        SELECT 
            u.name as umkmName,
            
            SUM(o.amount) as totalSales,
            COUNT(o.id) as totalOrders
        FROM orders o
        JOIN umkm u ON o.umkm_id = u.id
        WHERE u.kecamatan = :kecamatan 
          AND o.order_time >= NOW() - INTERVAL '24 hours'
        GROUP BY u.id, u.name
        ORDER BY totalSales DESC
        LIMIT 5
        """, nativeQuery = true)
    List<TopUmkmProjection> findTopUmkmByKecamatan(@Param("kecamatan") String kecamatan);

    @Query(value = """
        SELECT 
            u.name as umkmName,
            SUM(o.amount) as totalSales,
            COUNT(o.id) as totalOrders
        FROM orders o
        JOIN umkm u ON o.umkm_id = u.id
        WHERE o.order_time >= NOW() - INTERVAL '24 hours'
        GROUP BY u.id, u.name
        ORDER BY totalSales DESC
        LIMIT 5
        """, nativeQuery = true)
    List<TopUmkmDto> findTopUmkm();

    // Query 2: Trend penjualan per jam - pake window function
    @Query(value = """
        SELECT 
            DATE_TRUNC('hour', order_time) as hour,
            SUM(amount) as hourlySales,
            SUM(SUM(amount)) OVER (ORDER BY DATE_TRUNC('hour', order_time)) as runningTotal
        FROM orders o
        JOIN umkm u ON o.umkm_id = u.id
        WHERE u.kecamatan = :kecamatan
          AND order_time >= NOW() - INTERVAL '7 days'
        GROUP BY 1
        ORDER BY 1 DESC
        LIMIT 24
        """, nativeQuery = true)
    List<HourlySalesProjection> findHourlyTrend(@Param("kecamatan") String kecamatan);


    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.orderTime >= :startOfDay")
    Long sumAmountByTimestampAfter(@Param("startOfDay") LocalDateTime startOfDay);

    Long countByOrderTimeAfter(LocalDateTime orderTimeAfter);

//    @Query("SELECT new com.jakumkm.analytics.dto.TopUmkmDto(o.umkmName, SUM(o.amount)) " +
//            "FROM Order o WHERE o.timestamp >= :startOfDay " +
//            "GROUP BY o.umkmName ORDER BY SUM(o.amount) DESC")
//    List<TopUmkmDto> findTop5ByTimestampAfter(@Param("startOfDay") LocalDateTime startOfDay, Pageable pageable);
//    List<Trend24hDto> findTop5ByTimestampAfter(LocalDateTime startOfDay, PageRequest of);

    @Query(value = "SELECT DATE_TRUNC('hour', o.order_time) as hour, SUM(o.amount) as total \n" +
            "FROM orders o join umkm u on u.id =o.umkm_id \n" +
            "WHERE o.order_time >= :start AND (:kecamatan = 'all' OR u.kecamatan = :kecamatan) \n" +
            "GROUP BY DATE_TRUNC('hour', o.order_time) ORDER BY hour", nativeQuery = true)
    List<Trend24hDto> findTrend24hRaw(@Param("start") LocalDateTime start, @Param("kecamatan") String kecamatan);
}
