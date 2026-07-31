package com.asep.streamprocessor.topology;

import com.asep.streamprocessor.model.Order;
import com.asep.streamprocessor.util.OrderSerde;
import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author Asep Sudrajat
 * @since 29/7/2026
 */

@Component
public class UmkmTopology {

    private static final Logger log =
            LoggerFactory.getLogger(UmkmTopology.class);
    private final Serde<Order> orderSerde = OrderSerde.serde();

@Autowired
    public void buildPipeline(
            StreamsBuilder builder,
            RedisTemplate<String, String> redisTemplate) {

        log.info("=======================================");
        log.info("UMKM Kafka Streams Started");
        log.info("=======================================");

        KStream<String, Order> orders =
                builder.stream(
                        "umkm-orders",
                        Consumed.with(
                                Serdes.String(),
                                orderSerde
                        )
                );

        orders.filter((key, order) -> {
                    if (order == null) {
                        log.warn("Skip null order");
                        return false;
                    }
                    return true;
                })
                .filter((key, order) -> {

                    if (order.getKecamatan() == null ||
                            order.getKecamatan().isBlank()) {
                        log.warn("Invalid kecamatan : {}", order);
                        return false;
                    }
                    if (order.getAmount() == null ||
                            order.getAmount() <= 0) {
                        log.warn("Invalid amount : {}", order);
                        return false;
                    }
                    return true;

                });



                orders.foreach((key, order) -> {
                    try {
                        // Buat Map sesuai keinginan Anda menggunakan data order yang asli
                        java.util.Map<String, Object> orderEvent = java.util.Map.of(
                                "umkmName", order.getUmkmName(),    // Ambil langsung dari objek Order
                                "amount", order.getAmount()     // Ambil nilai transaksi saat ini
                        );

                        String orderJsonString = new Gson().toJson(orderEvent);

                        // Masukkan ke List antrean Redis dan batasi 10 item terbaru
                        redisTemplate.opsForList().leftPush("recent:orders", orderJsonString);
                        redisTemplate.opsForList().trim("recent:orders", 0, 9);

                        // Siarkan langsung ke channel topic:recent-orders
                        redisTemplate.convertAndSend("topic:recent-orders", orderJsonString);

                        log.info("Pushed to recent:orders -> {}", orderJsonString);
                    } catch (Exception e) {
                        log.error("Failed to push recent order to Redis", e);
                    }
                });


                orders.groupBy(
                        (key, order) -> {
//                            log.info(
//                                    "Grouping Order -> Kecamatan={}",
//                                    order.getKecamatan()
//                            );
                            return order.getKecamatan();
                        },
                        Grouped.with(
                                Serdes.String(),
                                orderSerde
                        )
                )
                //-------------------------------------------------------
                // Window 1 Minute
                //-------------------------------------------------------
                .windowedBy(
                        TimeWindows.ofSizeAndGrace(
                                Duration.ofMinutes(1),
                                Duration.ofSeconds(30)
                        )
                )
                .aggregate(
                        () -> 0L,
                        (kecamatan, order, total) -> {
                            long newTotal =
                                    total + order.getAmount();
//                            log.info(
//                                    "Aggregate [{}] Previous={} Amount={} New={}",
//                                    kecamatan,
//                                    total,
//                                    order.getAmount(),
//                                    newTotal
//                            );
                            return newTotal;
                        },

                        Materialized
                                .<String, Long, WindowStore<Bytes, byte[]>>as("sales-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Long())
                )
                .toStream()
                .foreach((windowedKey, totalSales) -> {
                    try {
                        String kecamatan =
                                windowedKey.key();

                        Instant start =
                                Instant.ofEpochMilli(
                                        windowedKey.window().start()
                                );
                        Instant end =
                                Instant.ofEpochMilli(
                                        windowedKey.window().end()
                                );
                        String redisKey ="top:sales:" + kecamatan;
                        redisTemplate.opsForValue().set(redisKey,String.valueOf(totalSales), Duration.ofMinutes(5));

                        redisTemplate.opsForValue().increment("summary:totalSalesToday", totalSales);
                        redisTemplate.opsForValue().increment("summary:totalOrdersToday", 1);

                        String currentSales = redisTemplate.opsForValue().get(redisKey);

//                        Map<String, String> salesMap = Map.of(kecamatan, currentSales);
//                        redisTemplate.convertAndSend("topic:realtime-map", new Gson().toJson(salesMap));

                        Map<String, String> payloadMap = Map.of(kecamatan, currentSales != null ? currentSales : "0");
                        System.out.println("go >> "+payloadMap);
                        redisTemplate.convertAndSend("topic:realtime-map", new Gson().toJson(payloadMap));

//
//                       Map<String, Object> orderEvent = Map.of(
//                                "umkmName", kecamatan,
//                                "totalSales", totalSales
//                        );
//                        String orderJsonString = new Gson().toJson(orderEvent);

                        // 3. Masukkan ke dalam antrean List Redis (Maksimal simpan 10 data terbaru)
//                        redisTemplate.opsForList().leftPush("recent:orders", orderJsonString);
//                        redisTemplate.opsForList().trim("recent:orders", 0, 9); // Batasi hanya 10 item
//
//                        // 4. Broadcast JSON String tersebut ke topic Pub/Sub Redis
//                        redisTemplate.convertAndSend("topic:recent-orders", orderJsonString);

//                        log.info(
//                                """
//                                ======================================
//                                Redis Updated
//                                --------------------------------------
//                                Kecamatan : {}
//                                Total     : {}
//                                Redis Key : {}
//                                Window    : {} -> {}
//                                ======================================
//                                """,
//                                kecamatan,
//                                totalSales,
//                                redisKey,
//                                start,
//                                end
//                        );

                    } catch (Exception e) {
                        log.error(
                                "Failed update Redis",
                                e
                        );

                    }
                });
        log.info("Topology build successfully.");
    }
}