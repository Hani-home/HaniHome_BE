package org.hanihome.hanihomebe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String health(){
        return "It's healthy";
    }
    @GetMapping("/health/authenticated")
    public String authenticated(){
        return "OK";
    }
}
