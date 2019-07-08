package com.lone.controller;

import com.lone.config.FilterPathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.controller
 * @ClassName: HelloController
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/4 20:00
 */

@RestController
public class HelloController {
//    @Autowired
//    AmqpTemplate amqpTemplate;

    @Autowired
    FilterPathConfig filterPathConfig;
    @GetMapping("/hello")
    public String hello(){
        String message = "hello " + new Date();

//        amqpTemplate.convertAndSend("inExchange", "routingKey_in", message);

        System.out.println(filterPathConfig.getPre());
        return new Date().toString();
    }
}
