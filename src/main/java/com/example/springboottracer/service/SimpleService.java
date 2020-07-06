package com.example.springboottracer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SimpleService {

    @Async
    public void doNothing(){
      log.info("inside doNothing");
      foo();
    }

    private void foo() {
        log.info("reached somewhere.. but where?");
    }
}
