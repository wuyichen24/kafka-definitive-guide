# Kafka Connect

## General Setup
### Step 1: Install Zookeeper And Kafka
### Step 2: Setup The Directory for Connector Plugins
- For connecting with a certain data store, you may use specific connector plugins. For loading those connector plugins, you have to create a directory.
  ```bash
  mkdir <kafka_root_directory>/plugins
  ```
- For letting Kafka Connect know where is the plugin directory, you have to change the configuration of Kafka Connect.
  **connect-distributed.properties**
  ```bash
  plugin.path=<kafka_root_directory>/plugins
  ```
