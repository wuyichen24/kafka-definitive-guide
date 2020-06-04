package com.kafkadefinitiveguide.producer.send;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Send messages synchronously.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class SynchronouslySend {
	public static void main(String[] args) {
		Properties kafkaProps = new Properties();
		kafkaProps.put("bootstrap.servers", "localhost:9092,localhost:9092");
		kafkaProps.put("key.serializer",    "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProps.put("value.serializer",  "org.apache.kafka.common.serialization.StringSerializer");
		
		ProducerRecord<String, String> record = new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
		
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps)) {
			producer.send(record).get();         // get() will wait for a reply from Kafka and will throw an exception if the record is not sent successfully to Kafka.
		} catch (Exception e) {
			// If the producer encountered errors before sending the message to Kafka.
			e.printStackTrace();
		}
	}
}
