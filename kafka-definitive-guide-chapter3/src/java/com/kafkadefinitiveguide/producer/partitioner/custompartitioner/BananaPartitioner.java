package com.kafkadefinitiveguide.producer.partitioner.custompartitioner;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

/**
 * Custom partitioner for route specify keys to certain partition.
 *
 * @author  Wuyi Chen
 * @date    06/03/2020
 * @version 1.0
 * @since   1.0
 */
public class BananaPartitioner implements Partitioner {
    public void configure(Map<String, ?> configs) {}

    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if ((keyBytes == null) || (!(key instanceof String))) {                // Only expect String keys, so we throw an exception if that is not the case.
            throw new IllegalArgumentException("We expect all messages to have customer name as key");
        }

        if (((String) key).equals("Banana")) {                                 // Banana will always go to last partition
            return numPartitions - 1;                
        }

        return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1));      // Other records will get hashed to the rest of the partitions
    }

    public void close() {}
}
