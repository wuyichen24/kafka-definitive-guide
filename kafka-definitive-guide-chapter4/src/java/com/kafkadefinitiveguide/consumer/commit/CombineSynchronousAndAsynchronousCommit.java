package com.kafkadefinitiveguide.consumer.commit;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combine synchronous commit with asynchronous commit.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class CombineSynchronousAndAsynchronousCommit {
	private static Logger logger = LoggerFactory.getLogger(CombineSynchronousAndAsynchronousCommit.class);
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",  "localhost:9092");
		props.put("group.id",           "CountryCounter");
		props.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("enable.auto.commit", "false");                                                  // Disable automatic commit 

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		consumer.subscribe(Collections.singletonList("customerCountries"));
		
		try {
		    while (true) {
		        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
		        for (ConsumerRecord<String, String> record : records) {
		            System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s%n",
		            		record.topic(), record.partition(), record.offset(), record.key(), record.value());
		        }
		        consumer.commitAsync();                                                            // Use commitAsync() first because of it is faster
		    }
		} catch (Exception e) {
		    logger.error("Unexpected error", e);
		} finally {
		    try {
		        consumer.commitSync();                                                             // Use commitSync() as retry
		    } finally {
		        consumer.close();
		    }
		}
	}
}
