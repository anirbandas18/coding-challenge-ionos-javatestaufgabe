package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude = {JmsAutoConfiguration.class, ActiveMQAutoConfiguration.class, RabbitAutoConfiguration.class})
public class DownloadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownloadServiceApplication.class, args);
    }

}
