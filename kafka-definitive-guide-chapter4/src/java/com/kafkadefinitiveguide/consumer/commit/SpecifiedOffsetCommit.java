package com.kafkadefinitiveguide.consumer.commit;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * Commit a specific offset.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class SpecifiedOffsetCommit {
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
			Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();               // Use this map to manually track offsets
			int count = 0;
			
			while (true) {
			    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			    for (ConsumerRecord<String, String> record : records) {
			        System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s%n",
			        		record.topic(), record.partition(), record.offset(), record.key(), record.value());
			        currentOffsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset()+1, "no metadata"));   // Update the offsets map with the offset of the next message we expect to process. 
			        if (count % 1000 == 0) {
			            consumer.commitAsync(currentOffsets, null);                                                                                        // Commit current offsets every 1,000 records
			        }
			        count++;
			    }
			}
		} finally {
		    consumer.close();
		}
	}
}
