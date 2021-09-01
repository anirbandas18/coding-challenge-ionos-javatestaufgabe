package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories
@EnableEurekaClient
public class AuftraegeServiceApplication {

    public static void main(String[] args) {
        Map<String,String> env = System.getenv();
        for(String k : env.keySet()) {
            System.out.println(k + " = " + env.get(k));
        }
        SpringApplication.run(AuftraegeServiceApplication.class, args);
    }

}
