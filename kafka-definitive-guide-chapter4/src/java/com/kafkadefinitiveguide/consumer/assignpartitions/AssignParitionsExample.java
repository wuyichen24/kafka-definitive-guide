package com.kafkadefinitiveguide.consumer.assignpartitions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

/**
 * Example of assigning the consumer to partitions instead of scribing a topic.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class AssignParitionsExample {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",  "localhost:9092");
		props.put("group.id",           "CountryCounter");                                                   // Specifies the consumer group the KafkaConsumer instance belongs to.
		props.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		List<PartitionInfo> partitionInfos = null;
		partitionInfos = consumer.partitionsFor("customerCountries");                                        // Get all the available partitions for that topic.

		List<TopicPartition> partitions = new ArrayList<>();
		if (partitionInfos != null) {   
		    for (PartitionInfo partition : partitionInfos) {
		        partitions.add(new TopicPartition(partition.topic(), partition.partition()));
		    }
		    consumer.assign(partitions);                                                                     // Assign the consumer to specific partitions instead of scribing a topic.           

		    while (true) {
		        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

		        for (ConsumerRecord<String, String> record: records) {
		            System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s%n",
		                record.topic(), record.partition(), record.offset(), record.key(), record.value());
		        }
		        consumer.commitSync();
		    }
		}
	}
}
