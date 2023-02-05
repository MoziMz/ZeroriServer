package com.mozi.moziserver.config;

import com.mozi.moziserver.common.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ADMIN_ORIGIN_URL}")
    private String origin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(origin)
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
