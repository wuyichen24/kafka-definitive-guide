package com.kafkadefinitiveguide.consumer.commit;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asynchronously commit the offset with the callback function for handling the response.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class AsynchronousCommitWithCallback {
	private static Logger logger = LoggerFactory.getLogger(AsynchronousCommitWithCallback.class);
	
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
			    consumer.commitAsync(new OffsetCommitCallback() {
			        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
			            if (e != null)
			                logger.error("Commit failed for offsets {}", offsets, e);              // If the commit fails, the failure and the offsets will be logged.
			        }
			    });
			}
		} finally {
		    consumer.close();
		}
	}
}
