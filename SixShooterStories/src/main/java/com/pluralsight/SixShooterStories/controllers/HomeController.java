package com.pluralsight.SixShooterStories.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    
    @GetMapping("/home")
    public String home() {
        return "forward:/index.html";
    }
    
    @GetMapping("/stories")
    public String stories() {
        return "forward:/index.html";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "forward:/index.html";
    }
}
