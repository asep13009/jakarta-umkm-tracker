package com.asep.streamprocessor.util;

import com.asep.streamprocessor.model.Order;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
public class OrderSerializer implements Serializer<Order> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Order data) {

        if (data == null) {
            return null;
        }

        try {
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Order", e);
        }
    }
}
