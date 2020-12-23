package com.minifiednd_api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreatureController {

    private static final String template = "Creature_%d";
    private final AtomicLong counter = new AtomicLong();
    CreatureService creatureService = new CreatureService( "bolt://minifiednd.com:7687", "neo4j", "goblinMonkeyBaby");

    @GetMapping("/creature")
    public Creature getCreature(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Creature(counter.incrementAndGet(), String.format(template, counter.get()), "mm", "medium", "LG", "1/4");
    }

    @GetMapping("/allCreatures")
    public List<Map<String, Object>> getAllCreatures() {
        return creatureService.getAllCreatures();
    }
}