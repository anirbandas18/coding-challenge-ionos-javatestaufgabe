package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed;


import com.teenthofabud.core.common.factory.TOABFeignErrorDecoderFactory;
import feign.codec.ErrorDecoder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableFeignClients(basePackages = { "com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy",
                                    "com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy"})
@EnableEurekaClient
@EnableBatchProcessing
@EnableScheduling
public class SeedBatchConfiguration {

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private ApplicationContext applicationContext;

    @Bean
    public ErrorDecoder errorDecoder() {
        String[] feignBasePackages = { "com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.auftraege.integration.proxy",
                "com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.integration.proxy"};
        return new TOABFeignErrorDecoderFactory(applicationContext, feignBasePackages);
    }

}
