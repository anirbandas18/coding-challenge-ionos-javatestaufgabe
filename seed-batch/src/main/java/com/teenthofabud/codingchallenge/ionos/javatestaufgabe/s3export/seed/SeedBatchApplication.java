package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude = {JmsAutoConfiguration.class, ActiveMQAutoConfiguration.class, RabbitAutoConfiguration.class})
public class SeedBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeedBatchApplication.class, args);
    }

}
