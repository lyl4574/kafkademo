package com.innodealing.kafkademo.kafka;
 
import java.util.List;
import java.util.Map;
 
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class MyPartition implements Partitioner {

	private static Logger LOG = LoggerFactory.getLogger(MyPartition.class);

	public MyPartition() {
	}

	@Override
	public void configure(Map<String, ?> configs) {

	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		int partitionNum = 0;
		try {
			partitionNum = Integer.parseInt((String) key);
		} catch (Exception e) {
			partitionNum = key.hashCode();
		}
//		LOG.info("the message sendTo topic:" + topic + "and key " + key + " and the partitionNum:" + (partitionNum % numPartitions));
		return Math.abs(partitionNum % numPartitions);
	}

	@Override
	public void close() {

	}
}
