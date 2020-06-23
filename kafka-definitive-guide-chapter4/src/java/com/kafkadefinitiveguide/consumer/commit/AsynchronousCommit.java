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
package com.kafkadefinitiveguide.consumer.commit;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Asynchronously commit the offset.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class AsynchronousCommit {
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
			            record.topic(), record.partition(), record.offset(),
			            record.key(), record.value());
			    }
			    consumer.commitAsync();                                                            // Commit the last offset and carry on
			}
		} finally {
		    consumer.close();
		}
	}
}
