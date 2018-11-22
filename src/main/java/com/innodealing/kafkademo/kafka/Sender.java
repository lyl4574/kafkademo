package com.innodealing.kafkademo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class Sender {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    @Qualifier("jsonKafkaTemplate")
    private KafkaTemplate jsonKafkaTemplate;

    @Value("${app.topic.test}")
    private String topic;

    public void send(String message,User user){
//        LOG.info("sending message='{}' to topic='{}'", message, topic);
//        kafkaTemplate.send(topic, message);

//        LOG.info("sending message='{}' to topic='{}'", a, topic);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = jsonKafkaTemplate.send(topic,user.getId(),user);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("发送失败啦");
            }

            @Override
            public void onSuccess(SendResult<Integer, String> sendResult) {
                System.out.println("发送成功，" + sendResult.getProducerRecord().key());
            }
        });

    }
}