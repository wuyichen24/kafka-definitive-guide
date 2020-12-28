# kafka-definitive-guide

[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0) 

This repository contains the sample code the book "[Kafka: The Definitive Guide (Gwen Shapira, Neha Narkhede, and Todd Palino)](http://shop.oreilly.com/product/0636920044123.do)" and the personal study note of Apache Kafka.

![](docs/pics/book-cover.jpg)

## Study Notes
- [Google Doc](https://docs.google.com/document/d/1JJqllxpVwzTJLrGILxJ10LT5_lhi8ZbKlHcrE54A6Rc/edit?usp=sharing)

## Documentation
- [Avro](avro/README.md)
- [Kafka Connect](docs/kafka_connect.md)
- [Command Memo](docs/command_memo.md)
- [Installation](docs/installation.md)

## Package Introduction
### Chapter 3
| Package | Description |
|----|----|
| `com.kafkadefinitiveguide.producer.send` | The basic examples of producers for sending messages in different styles: fire-and-forget, synchronous and asynchronous. |
| `com.kafkadefinitiveguide.producer.serializer.customserializer` | The example of writing a custom serializer for a POJO class. |
| `com.kafkadefinitiveguide.producer.serializer.avroserializer` | The example of sending messages by using Avro serializer. |
| `com.kafkadefinitiveguide.producer.partitioner.custompartitioner` | The example of writing a custom partitioner. |

### Chapter 4
| Package | Description |
|----|----|
| `com.kafkadefinitiveguide.consumer.receive` | The basic example of a consumer. |
| `com.kafkadefinitiveguide.consumer.commit` | The examples of different offset commit strategies: synchronous, asynchronous, combination of synchronous and asynchronous, specifying offset. |
| `com.kafkadefinitiveguide.consumer.rebalancelisteners` | The examples of using rebalance listeners. |
| `com.kafkadefinitiveguide.consumer.exitpollloop` | The example of how to exit a poll loop safely. |
| `com.kafkadefinitiveguide.consumer.deserializer.customdeserializer` | The example of writing a custom deserializer for a POJO class. |
| `com.kafkadefinitiveguide.consumer.deserializer.avrodeserializer` | The example of receiving messages by using Avro deserializer. |
| `com.kafkadefinitiveguide.consumer.assignpartitions` | The example of assigning a consumer to partitions instead of letting the consumer subscribing a topic. |
