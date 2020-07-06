package com.example.springboottracer.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfiguration implements AsyncConfigurer {

    private ApplicationEventPublisher eventPublisher;

    public AsyncConfiguration(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /* Uncomment to make application start
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.initialize();
        return scheduler;
    }
    */

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler(eventPublisher);
    }
}
