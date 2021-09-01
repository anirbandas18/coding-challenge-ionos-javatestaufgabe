package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class KundeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KundeServiceApplication.class, args);
    }

}
