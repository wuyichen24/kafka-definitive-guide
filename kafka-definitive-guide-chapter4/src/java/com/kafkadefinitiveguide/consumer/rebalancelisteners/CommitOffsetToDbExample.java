package com.kafkadefinitiveguide.consumer.rebalancelisteners;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class CommitOffsetToDbExample {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",  "localhost:9092");
		props.put("group.id",           "CountryCounter");
		props.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("enable.auto.commit", "false");                                                            // Disable automatic commit.

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {
		    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		        commitDBTransaction();                                                                       // Commit the transactions to DB before losing the ownership of the partition.
		    }

		    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		        for(TopicPartition partition: partitions) {
		            consumer.seek(partition, getOffsetFromDB(partition));                                    // Get the offset of each partition and use seek() to correct the offset of each partition.
		        }
		    }
		}

		consumer.subscribe(Collections.singletonList("customerCountries"), new SaveOffsetsOnRebalance());
		consumer.poll(Duration.ofMillis(0));                                                                 // Make sure this consumer join a consumer group and get assigned partitions.

		for (TopicPartition partition: consumer.assignment()) {
		    consumer.seek(partition, getOffsetFromDB(partition));                                            // Correct offset in the partitions we are assigned to.
		}
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, String> record : records) {
					processRecord(record);                                                                   // Process the record.
					storeRecordInDB(record);                                                                 // Store the record into DB.
					storeOffsetInDB(record.topic(), record.partition(), record.offset());                    // Store the new offset into DB.
				}
				commitDBTransaction();
			}
		} finally {
		    consumer.close();
		}
	}
	
	private static void commitDBTransaction() {}
	private static OffsetAndMetadata getOffsetFromDB(TopicPartition partition) { return null; }
	private static void processRecord(ConsumerRecord<String, String> record) {}
	private static void storeRecordInDB(ConsumerRecord<String, String> record) {}
	private static void storeOffsetInDB(String topic, int parition, long offset) {}
}
