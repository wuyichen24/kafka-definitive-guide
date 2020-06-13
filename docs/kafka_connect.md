# Kafka Connect

## General Setup
### Step 1: Install Zookeeper And Kafka
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
