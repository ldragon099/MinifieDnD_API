package com.minifiednd_api;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreatureController {

    private static final String template = "Creature_%d";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/creature")
    public Creature creature(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Creature(counter.incrementAndGet(), String.format(template, counter.get()), "mm", "medium", "LG", "1/4");
    }
}