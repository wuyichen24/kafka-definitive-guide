package com.kafkadefinitiveguide.producer.send;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Send messages in fire-and-forget style.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class FireAndForgetSend {
	public static void main(String[] args) {
		Properties kafkaProps = new Properties();
		kafkaProps.put("bootstrap.servers", "localhost:9092,localhost:9092");
		kafkaProps.put("key.serializer",    "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProps.put("value.serializer",  "org.apache.kafka.common.serialization.StringSerializer");
		
		ProducerRecord<String, String> record = new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
		
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps)) {
			producer.send(record);         // Ignore the returned value, no way to know the message was sent successfully or not.
		} catch (Exception e) {
			// If the producer encountered errors before sending the message to Kafka.
			e.printStackTrace();
		}
	}
}
