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
### Step 4: Run Kafka Connect Worker
  ```bash
  cd bin
  sh connect-distributed.sh ../config/connect-distributed.properties
  ```
### Step 5: Manage Connectors by Kafka Connect REST APIs
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
### Step 1: Download And Deploy MySQL Connector Plugin
- Download the Debezium MySQL Connector plugin from [here](https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/).
- Extract the Debezium MySQL Connector plugin in the plugins directory.
  ```bash
  cd <kafka_root_directory>/plugins
  tar -zxf debezium-connector-mysql-1.1.2.Final-plugin.tar.gz
  ```
### Step 2: Enable MySQL Binary Logging (binlog)
- MySQL binary logging is not enabled by default. You have to enable it manually.
- Add 2 properties into the `mysqld` section of the `my.cnf`.
  ```cnf
  [mysqld]
  log-bin=mysql-bin
  server-id=1
  ```
- Restart MySQL server.
- Verify the binary logging is enabled by checking the `log_bin` variable is `ON`.
  ```sql
  show variables like 'log_bin';
  ```
- Troubleshooting
   - You may need to create a new `my.cnf` file if it is not existing.
   - You can specify the path of the new `my.cnf` file in MySQL Workbench by clicking the wrench icon next to "INSTANCE".
   - You can use MySQL Workbench to create a new `my.cnf` file by clicking the "Options File" under "INSTANCE".
   - If restarting MySQL server cannot apply the new parameter values of the `my.cnf` file into the system variables, you need to start MySQL server by command-line.
     ```bash
     sudo /usr/local/mysql/bin/mysqld_safe
     (Press Control-Z)
     bg
     ```
### Step 3: Create Database And Tables (For Demo Only)
- Assume you have database (schema): testdb
- Assume you have tables
   - customers
   - order

### Step 4: Create Connector
- Compose the connector configration in JSON
  ```json
  {
    "name": "mysql-source-connector",
    "config": {
      "connector.class": "io.debezium.connector.mysql.MySqlConnector",
      "tasks.max": "1",
      "database.hostname": "localhost",
      "database.port": "3306",
      "database.user": "root",
      "database.password": "6ytow2-;S3lA",
      "database.server.id": "001",
      "database.server.name": "mysqlserver1",
      "database.whitelist": "testdb",
      "database.serverTimezone": "UTC",
      "database.history.kafka.bootstrap.servers": "localhost:9092",
      "database.history.kafka.topic": "schema-changes.testdb"  
    }
  }
  ```
- Explanation of parameters
  | Parameter | Description |
  |---|---|
  | `name` | The unique name of the connector. |
  | `connector.class` | The name of the Java class for the connector. Always use a value of `io.debezium.connector.mysql.MySqlConnector` for the MySQL connector. |
  | `tasks.max` | The maximum number of tasks that should be created for this connector. The MySQL connector always uses a single task and therefore does not use this value, so the default is always acceptable. |
  | `database.hostname` | The IP address or hostname of the MySQL database server. |
  | `database.port` | The port of the MySQL database server. |
  | `database.user` | The username for connecting the MySQL database server. |
  | `database.password` | The password for connecting the MySQL database server. |
  | `database.server.id` | The numeric ID of the MySQL database server. |
  | `database.server.name` | The name of the MySQL database server. This name is the logical identifier for the MySQL server or cluster of servers. This name will be used as the prefix for all Kafka topics. |
  | `database.whitelist` | The list of databases (schemas) will be monitored. (comma-separated) | 
  | `database.blacklist` | The list of databases (schemas) will be be excluded from monitoring. (comma-separated) | 
  | `table.whitelist` | The list of tables will be monitored. (comma-separated) | 
  | `table.blacklist` | The list of tables will be be excluded from monitoring. (comma-separated) | 
  | `database.serverTimezone` | The timezone for the MySQL database server. If not specify, the MySQL database server will throw an error like "The server time zone value 'XXX' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the serverTimezone configuration property) to use a more specifc time zone value if you want to utilize time zone support." |
  | `database.history.kafka.bootstrap.servers` | The list of the hostname and port pairs for Kafka brokers. |
  | `database.history.kafka.topic` | The topic to store the schema change history of the database. |
- For more parameters, check this [page](https://debezium.io/documentation/reference/1.1/connectors/mysql.html#mysql-connector-configuration-properties_debezium).
