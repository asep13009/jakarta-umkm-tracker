package com.asep.streamprocessor.util;

import com.asep.streamprocessor.model.Order;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * @author Asep Sudrajat
 * @since 30/7/2026
 */
public class OrderSerde {

    public static Serde<Order> serde() {
        return Serdes.serdeFrom(
                new OrderSerializer(),
                new OrderDeserializer()
        );
    }

}