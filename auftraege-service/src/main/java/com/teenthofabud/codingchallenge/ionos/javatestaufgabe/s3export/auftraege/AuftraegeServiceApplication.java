package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.auftraege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AuftraegeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuftraegeServiceApplication.class, args);
    }

}
