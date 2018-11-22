package com.innodealing.kafkademo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);
//containerFactory = "kafkaListenerContainerFactory",

//    @KafkaListener(topics = "${app.topic.test}")
//    public void receive(@Payload String message,
//                        @Headers MessageHeaders headers) {
//        LOG.info("received message='{}'", message);
//        headers.keySet().forEach(key -> LOG.info("{}: {}", key, headers.get(key)));
//    }

//    @KafkaListener(topics = "${app.topic.test}",errorHandler="myKafkaListenerErrorHandler")
//    public void receive1(ConsumerRecord record) {
//        LOG.info("received message1='{}'",((User) record.value()).getName());
////        headers.keySet().forEach(key -> LOG.info("{}: {}", key, headers.get(key)));
//    }
    @KafkaListener(topics = "${app.topic.test}",topicPartitions={@TopicPartition(topic = "${app.topic.test}",partitions = {"0"})},groupId = "topic2_a",errorHandler="myKafkaListenerErrorHandler")
    public void receive1(ConsumerRecord record) {
        LOG.info("received message0='{}'",((User) record.value()).getName());
//        headers.keySet().forEach(key -> LOG.info("{}: {}", key, headers.get(key)));
    }

    @KafkaListener(topics = "${app.topic.test}",topicPartitions={@TopicPartition(topic = "${app.topic.test}",partitions = {"1"})},groupId = "topic2_b",errorHandler="myKafkaListenerErrorHandler")
    public void receive2(ConsumerRecord record) {
        LOG.info("received message1='{}'",((User) record.value()).getName());
//        headers.keySet().forEach(key -> LOG.info("{}: {}", key, headers.get(key)));
    }
    @KafkaListener(topics = "${app.topic.test}",topicPartitions={@TopicPartition(topic = "${app.topic.test}",partitions = {"2","3","4","5","6","7"})},groupId = "topic2_default",errorHandler="myKafkaListenerErrorHandler")
    public void receiveDefault(ConsumerRecord record) {
        LOG.info("received message default='{}'",((User) record.value()).getName());
//        headers.keySet().forEach(key -> LOG.info("{}: {}", key, headers.get(key)));
    }
}