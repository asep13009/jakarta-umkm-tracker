package com.asep.streamprocessor.util;

import com.asep.streamprocessor.model.Order;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */


public class OrderDeserializer implements Deserializer<Order> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Order deserialize(String topic, byte[] data) {

        if (data == null) {
            return null;
        }

        try {
            return mapper.readValue(data, Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize Order", e);
        }
    }
}
