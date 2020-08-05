package com.example.springboottracer;

import io.opentracing.contrib.spring.integration.messaging.OpenTracingChannelInterceptorAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {OpenTracingChannelInterceptorAutoConfiguration.class})
public class SpringbootTracerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTracerApplication.class, args);
    }

}
