package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.naming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaServer
public class NamingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NamingServiceApplication.class, args);
    }

}
