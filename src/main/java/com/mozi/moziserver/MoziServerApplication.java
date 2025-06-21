package com.mozi.moziserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableAsync
@EnableJpaAuditing
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class MoziServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoziServerApplication.class, args);
    }

}
