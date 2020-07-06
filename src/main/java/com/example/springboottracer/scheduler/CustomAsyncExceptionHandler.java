package com.example.springboottracer.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessResourceFailureException;

import java.lang.reflect.Method;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    public CustomAsyncExceptionHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        if (throwable instanceof DataAccessResourceFailureException) {
            log.error("Uncaught!!! {}", throwable.getMessage());
            AvailabilityChangeEvent.publish(this.eventPublisher, throwable, LivenessState.BROKEN);
        }
    }
}
