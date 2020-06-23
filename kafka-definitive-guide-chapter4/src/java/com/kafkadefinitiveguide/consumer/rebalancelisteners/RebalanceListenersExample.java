/*
 * Copyright 2020 Wuyi Chen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kafkadefinitiveguide.consumer.rebalancelisteners;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The example code of committing the offset after the consumer stopped consuming messages 
 * but before losing ownership of a partition.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class RebalanceListenersExample {
	private static Logger logger = LoggerFactory.getLogger(RebalanceListenersExample.class);
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",  "localhost:9092");
		props.put("group.id",           "CountryCounter");
		props.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("enable.auto.commit", "false");                                                            // Disable automatic commit.

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
		
		class HandleRebalance implements ConsumerRebalanceListener {
		    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		    	/* Nothing needs to do when this consumer is assigned a new partition */
		    }

		    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {                         // Commit the offset before losing the ownership of the partition.
		        System.out.println("Lost partitions in rebalance. " + "Committing current offsets:" + currentOffsets);
		        consumer.commitSync(currentOffsets);
		    }
		}
		
		try {
		    consumer.subscribe(Collections.singletonList("customerCountries"), new HandleRebalance());       // Pass the ConsumerRebalanceListener to the subscribe() method so it will get invoked by the consumer.

		    while (true) {
		        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
		        for (ConsumerRecord<String, String> record : records) {
		            System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s%n",
		            		record.topic(), record.partition(), record.offset(), record.key(), record.value());
		             currentOffsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset()+1, null));
		        }
		        consumer.commitAsync(currentOffsets, null);
		    }
		} catch (WakeupException e) {
		    // ignore, we're closing
		} catch (Exception e) {
		    logger.error("Unexpected error", e);
		} finally {
		    try {
		        consumer.commitSync(currentOffsets);
		    } finally {
		        consumer.close();
		        System.out.println("Closed consumer and we are done");
		    }
		}
	}
}
