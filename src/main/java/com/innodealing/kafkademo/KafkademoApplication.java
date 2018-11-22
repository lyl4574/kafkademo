package com.innodealing.kafkademo;

import com.innodealing.kafkademo.kafka.Sender;
import com.innodealing.kafkademo.kafka.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkademoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KafkademoApplication.class, args);
    }

    @Autowired
    private Sender sender;

    @Override
    public void run(String... strings) throws Exception {
        int i=100000000;
        while(true) {
            for (int j = 0; j < 1000; j++) {
                User user = new User();
                user.setId(i);
                user.setName("lyl" + i);
                sender.send("Spring Kafka and Spring Boot Configuration Example", user);
//                Thread.sleep(10);
                i++;
            }
            Thread.sleep(60000);
        }
    }
}
