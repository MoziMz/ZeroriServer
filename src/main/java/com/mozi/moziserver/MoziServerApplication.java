package com.mozi.moziserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class MoziServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoziServerApplication.class, args);
    }

}
