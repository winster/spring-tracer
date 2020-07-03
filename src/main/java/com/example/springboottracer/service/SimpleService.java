package com.example.springboottracer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SimpleService {

    public void doNothing(){
      log.info("inside doNothing");
    }
}
