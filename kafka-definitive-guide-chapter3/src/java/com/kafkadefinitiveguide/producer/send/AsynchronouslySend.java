package com.kafkadefinitiveguide.producer.send;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Send messages asynchronously.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class AsynchronouslySend {
	public static void main(String[] args) {
		Properties kafkaProps = new Properties();
		kafkaProps.put("bootstrap.servers", "localhost:9092");
		kafkaProps.put("key.serializer",    "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProps.put("value.serializer",  "org.apache.kafka.common.serialization.StringSerializer");
		
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps)) {
			ProducerRecord<String, String> record = new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA"); 
			producer.send(record, new DemoProducerCallback());    // Use callback to get result of sending asynchronously.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
