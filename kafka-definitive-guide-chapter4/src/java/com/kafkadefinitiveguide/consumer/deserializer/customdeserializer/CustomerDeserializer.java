package com.kafkadefinitiveguide.consumer.deserializer.customdeserializer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * The customer deserializer for deserializing Customer Pojo.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class CustomerDeserializer implements Deserializer<Customer> {
    @Override
    public void configure(Map configs, boolean isKey) {
        // nothing to configure
    }

    /* (non-Javadoc)
     * @see org.apache.kafka.common.serialization.Deserializer#deserialize(java.lang.String, byte[])
     * 
     * We are deserializing Customer as:
     *    - 4 byte int representing customerId
     *    - 4 byte int representing length of customerName in UTF-8 bytes (0 if name is Null)
     *    - N bytes representing customerName in UTF-8
     */
    @Override
    public Customer deserialize(String topic, byte[] data) {
        int id;
        int nameSize;
        String name;

        try {
            if (data == null) {
                return null;
            }
            if (data.length < 16) {
                throw new SerializationException("Size of data received " + "by deserializer is shorter than expected");
            }

            ByteBuffer buffer = ByteBuffer.wrap(data);
            id       = buffer.getInt();                    // 4 byte int representing customerId
            nameSize = buffer.getInt();                    // 4 byte int representing length of customerName in UTF-8 bytes (0 if name is Null)

            byte[] nameBytes = new byte[nameSize];         // N bytes representing customerName in UTF-8
            buffer.get(nameBytes);
            name = new String(nameBytes, "UTF-8");

            return new Customer(id, name);
        } catch (Exception e) {
        	throw new SerializationException("Error when deserializing byte[] to Customer " + e);
        }
    }

    @Override
    public void close() {
        // nothing to close
    }
}
