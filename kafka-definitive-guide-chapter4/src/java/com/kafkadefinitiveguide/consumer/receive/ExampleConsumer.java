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
package com.kafkadefinitiveguide.consumer.receive;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A basic example of a consumer.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class ExampleConsumer {
	private static Logger logger = LoggerFactory.getLogger(ExampleConsumer.class);
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",  "localhost:9092");
		props.put("group.id",           "CountryCounter");                                                   // Specifies the consumer group the KafkaConsumer instance belongs to.
		props.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		consumer.subscribe(Collections.singletonList("customerCountries"));                                  // Specifies the list of topics the KafkaConsumer instance subscribe to.
		
		Map<String, Integer> customerCountryMap = new HashMap<>();                                           // Captures the count of customers from each county.
		
		try {
		    while (true) { 
		        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));             // It is a timeout interval and controls how long poll() will block if data is not available in the consumer buffer.
		        for (ConsumerRecord<String, String> record : records) {
		            logger.debug("topic = {}, partition = {}, offset = {}, customer = {}, country = {}",
		                record.topic(), record.partition(), record.offset(), record.key(), record.value());

		            int updatedCount = 1;
		            if (customerCountryMap.containsKey(record.value())) {
		                updatedCount = customerCountryMap.get(record.value()) + 1;
		            }
		            customerCountryMap.put(record.value(), updatedCount);

		            JSONObject json = new JSONObject(customerCountryMap);
		            System.out.println(json.toString(4));
		        }
		    }
		} finally {
		    consumer.close();                                                                                // Close the consumer and will also trigger a rebalance immediately.
		}
	}
}
