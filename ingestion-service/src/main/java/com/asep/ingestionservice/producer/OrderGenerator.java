package com.asep.ingestionservice.producer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * @author Asep Sudrajat
 * @since 29/7/2026
 */

@Component
public class OrderGenerator {

    private final KafkaTemplate<String, String> kafkaTemplate;


    private final Random random = new Random();
    private static final Map<Integer, String> UMKM_MAP = Map.ofEntries(
            Map.entry(1, "Bakso Pakde Kemang"),
            Map.entry(2, "Kopi Kulo Thamrin"),
            Map.entry(3, "Seblak Presman"),
            Map.entry(4, "Ayam Gepuk Bang Jack"),
            Map.entry(5, "Warung Nasi Bu Tini"),
            Map.entry(6, "Sate Madura Cak Ali"),
            Map.entry(7, "Mie Ayam Pak Slamet"),
            Map.entry(8, "Es Teh Nusantara"),
            Map.entry(9, "Martabak Bang Udin"),
            Map.entry(10, "Roti Bakar Mantul")

    );
    private final List<Integer> umkmIds = List.copyOf(UMKM_MAP.keySet());
    private final List<String> kecamatans = List.of("Jakarta Selatan", "Jakarta Pusat", "Jakarta Barat", "Jakarta Timur", "Jakarta Utara");

    public OrderGenerator(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 5000) // tiap 2 detik
    public void generateOrder() {
        int selectedId = umkmIds.get(random.nextInt(umkmIds.size()));
        String umkmName = UMKM_MAP.get(selectedId);
        String kecamatan = kecamatans.get(random.nextInt(kecamatans.size()));
        int amount = 15000 + random.nextInt(50000);

        String orderJson = String.format("""
            {
              "orderId": "%s",
              "umkmId": %d,
              "umkmName": "%s",
              "kecamatan": "%s",
              "amount": %d,
              "timestamp": "%s"
            }
            """,
                java.util.UUID.randomUUID(),
                selectedId,
                umkmName,
                kecamatan,
                amount,
                LocalDateTime.now()
        );

        kafkaTemplate.send("umkm-orders", orderJson);
        System.out.println("Sent kafka umkm-orders: " + orderJson);
//        System.out.println("Sent kafka umkm-orders: " );
    }
}
