# Kafka Connect

- [**General Setup**](#general-setup)
   - [Install And Run Zookeeper And Kafka](#step-1-install-and-run-zookeeper-and-kafka)
   - [Setup The Directory for Connector Plugins](#step-2-setup-the-directory-for-connector-plugins)
   - [Configure Kafka Connect worker](#step-3-configure-kafka-connect-worker)
   - [Run Kafka Connect worker](#step-4-run-kafka-connect-worker)
   - [Manage connectors by Kafka Connect REST APIs](#step-5-manage-connectors-by-kafka-connect-rest-apis)

## General Setup
### Step 1: Install And Run Zookeeper And Kafka
### Step 2: Setup The Directory for Connector Plugins
- For connecting with a certain data store, you may use a specific connector plugin. For loading those connector plugins, you have to create a directory.
  ```bash
  mkdir <kafka_root_directory>/plugins
  ```
- For letting Kafka Connect know where is the plugin directory, you have to change the configuration of Kafka Connect worker.
  
  **connect-distributed.properties**
  ```bash
  plugin.path=<kafka_root_directory>/plugins
  ```
### Step 3: Configure Kafka Connect worker
- The Kafka Connect worker is configured by the configuration file `connect-distributed.properties`.
- The `config` directory has the `connect-distributed.properties` file by default, you can modify it directly.
- Avaliable properties:
  | Property | Description |
  |---|---|
  | `bootstrap.servers` | A list of Kafka brokers that Connect will work with. |
  | `group.id` | `All workers with the same group ID are part of the same Connect cluster.` |
  | `key.converter` | The converter for keys. |
  | `value.converter` | The converter for values. |
  | `key.converter.schemas.enable` | Enable the schema for the key converter. |
  | `value.converter.schemas.enable` | Enable the schema for the value converter. |
  | `rest.host.name` | The hostname of the REST API for configuring and monitoring Connect. |
  | `rest.port` | The port of the REST API for configuring and monitoring Connect. |
  | `plugin.path` | A list of directories for loading connector plugins. |
- Example of `connect-distributed.properties`
  ```properties
  bootstrap.servers=localhost:9092
  group.id=connect-cluster
  key.converter=org.apache.kafka.connect.json.JsonConverter
  value.converter=org.apache.kafka.connect.json.JsonConverter
  key.converter.schemas.enable=true
  value.converter.schemas.enable=true
  offset.storage.topic=connect-offsets
  offset.storage.replication.factor=1
  config.storage.topic=connect-configs
  config.storage.replication.factor=1
  status.storage.topic=connect-status
  status.storage.replication.factor=1
  offset.flush.interval.ms=10000
  plugin.path=/Users/wuyichen/kafka_2.11-2.2.0/plugins
  ```
### Step 4: Run Kafka Connect worker
  ```bash
  cd bin
  sh connect-distributed.sh ../config/connect-distributed.properties
  ```
### Step 5: Manage connectors by Kafka Connect REST APIs
- Common APIs
  | Method | URL | Body | Description |
  |---|---|---|---|
  | GET | `http://localhost:8083/` | | Check the worker is running. |
  | GET | `http://localhost:8083/connector-plugins` | | Display all the available connector plugins. |
  | POST | `http://localhost:8083/connectors` | Connector configuration (JSON) | Add a new connector. |
  | GET | `http://localhost:8083/connectors` | | Display all the running connectors. | 
  | GET | `http://localhost:8083/connectors/<connector_name>` | | Display the detailed info of a connector. |
  | DELETE | `http://localhost:8083/connectors/<connector_name>` | | Delete a connector. | 

## Connect to MySQL (Debezium)
### Download And Deploy MySQL Connector Plugin
- Download the Debezium MySQL Connector plugin from [here](https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/)
