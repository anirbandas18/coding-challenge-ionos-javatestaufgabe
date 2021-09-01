package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed;


import com.teenthofabud.core.common.factory.TOABFeignErrorDecoderFactory;
import feign.codec.ErrorDecoder;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

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

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCircuitBreakerFactory(
            @Value("${s3export.seed.circuit-breaker.failure.threshold-percentage}") float failureThresholdPercentage,
            @Value("${s3export.seed.circuit-breaker.wait.duration.in-open-state}") long waitDurationInOpenStateInMillis,
            @Value("${s3export.seed.circuit-breaker.sliding-window.size}") int slidingWindowSize,
            @Value("${s3export.seed.circuit-breaker.timeout.duration}") long timeoutDurationInSeconds) {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureThresholdPercentage)
                .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenStateInMillis))
                .slidingWindowSize(slidingWindowSize)
                .build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(timeoutDurationInSeconds))
                .build();
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

}
