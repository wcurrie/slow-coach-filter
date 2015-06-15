package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.Filter;

@Controller
@EnableAutoConfiguration
public class SimpleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/slow")
    @ResponseBody
    String slow() throws InterruptedException {
        Thread.sleep(1500);
        return "I'm slow... " + Thread.currentThread().getName();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SimpleController.class, args);
    }

    @Bean
    public Filter filter() {
        return new SlowCoachFilter();
    }
}
