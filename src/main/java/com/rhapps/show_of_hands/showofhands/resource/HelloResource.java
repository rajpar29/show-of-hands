package com.rhapps.show_of_hands.showofhands.resource;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloResource {

    @GetMapping("/all")
    public String hello(){
        return "hello";
    }

    @GetMapping("/secured/all")
    public String securedHello(){
        return "Secured Hello";
    }
}
