package com.innodealing.kafkademo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

//import org.springframework.kafka.listener.config.ContainerProperties;

@Configuration
@EnableKafka
public class Config<K, V>{
    @Value("${kafka.bootstrap-servers}")
    private String brokerAddress;
    @Bean
    public ProducerFactory<K, V> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ProducerConfig.LINGER_MS_CONFIG,5);//延迟5ms 发送
        props.put(ProducerConfig.RETRIES_CONFIG,3);//重试3次
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);// 批量消息字节数
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.innodealing.kafkademo.kafka.MyPartition");
        // set more properties
        return new DefaultKafkaProducerFactory<>(props);
    }
    @Bean
    public ConsumerFactory<K, V> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,5000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,CustomJsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//        props.put(ConsumerConfig.)
        // set more properties
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean("jsonKafkaTemplate")
    public KafkaTemplate<K, V> jsonKafkaTemplate() {
        KafkaTemplate<K, V> kt =  new KafkaTemplate(producerFactory());
//        MessagingMessageConverter mmc = new MessagingMessageConverter();
//        mmc.setHeaderMapper(new DefaultKafkaHeaderMapper());
//        kt.setMessageConverter(mmc);
        return kt;
    }
//    @Bean
//    public KafkaMessageDrivenChannelAdapter<String, String>
//    adapter(KafkaMessageListenerContainer<String, String> container) {
//        KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
//                new KafkaMessageDrivenChannelAdapter<>(container, ListenerMode.record);
//        kafkaMessageDrivenChannelAdapter.setOutputChannel(received());
//        return kafkaMessageDrivenChannelAdapter;
//    }
//
    @Bean
    public KafkaMessageListenerContainer<K, V> container() throws Exception {
        ContainerProperties properties = new ContainerProperties(Pattern.compile("test+"));
//        properties.set
//        properties.setErrorHandler(new SeekToCurrentErrorHandler());
        properties.setMessageListener((MessageListener) message->{
            System.out.println("message" +message);
        });
        // set more properties
        return new KafkaMessageListenerContainer(consumerFactory(), properties);
    }
//@Bean
//public ConcurrentKafkaListenerContainerFactory<K, V> kafkaListenerContainerFactory() {
//    ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory();
//    factory.setConsumerFactory(consumerFactory());
//    factory.getContainerProperties().setAckOnError(false);
//    factory.getContainerProperties().setErrorHandler(new SeekToCurrentErrorHandler());
//    factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.BATCH);
//    return factory;
//}

//    如果enable.auto.commit设置为true，那么kafka将自动提交offset。如果设置为false，则支持下列AckMode（确认模式）。
//
//    消费者poll()方法将返回一个或多个ConsumerRecords
//
//    RECORD ：处理完记录以后，当监听器返回时，提交offset
//    BATCH  ：当对poll()返回的所有记录进行处理完以后，提交偏offset
//    TIME   ：当对poll()返回的所有记录进行处理完以后，只要距离上一次提交已经过了ackTime时间后就提交
//    COUNT  ：当poll()返回的所有记录都被处理时，只要从上次提交以来收到了ackCount条记录，就可以提交
//    COUNT_TIME ：和TIME以及COUNT类似，只要这两个中有一个为true，则提交
//    MANUAL ：消息监听器负责调用Acknowledgment.acknowledge()方法，此后和BATCH是一样的
//    MANUAL_IMMEDIATE ：当监听器调用Acknowledgment.acknowledge()方法后立即提交
}
