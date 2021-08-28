package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude = {JmsAutoConfiguration.class, ActiveMQAutoConfiguration.class, RabbitAutoConfiguration.class})
public class SynchronizationBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronizationBatchApplication.class, args);
    }

}
