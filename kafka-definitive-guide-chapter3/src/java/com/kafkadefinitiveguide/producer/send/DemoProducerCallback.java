package com.kafkadefinitiveguide.producer.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Callback for asynchronous send.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class DemoProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }
}
