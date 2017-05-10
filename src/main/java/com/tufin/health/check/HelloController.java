package com.tufin.health.check;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/health")
public class HelloController {

    @RequestMapping("/plugins")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    @RequestMapping("/check/{test}")
    public String index1() {
        return "Greetings from Spring Boot!";
    }

}