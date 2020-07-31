package com.example.springboottracer.scheduler;

import com.example.springboottracer.service.SimpleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class SimpleJob {

    private final SimpleService simpleService;

    SimpleJob(SimpleService simpleService) {
        this.simpleService = simpleService;
    }

    @Scheduled(fixedRate = 30000L)
    public void job1() {
        log.info("inside job1");
        simpleService.doNothing();
    }

}
