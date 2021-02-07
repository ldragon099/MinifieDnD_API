package com.minifiednd_api.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.minifiednd_api.models.Creature;
import com.minifiednd_api.services.CreatureService;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public List<Object> getAllCreatures() {
        return creatureService.getAllCreatures();
    }

    @GetMapping("/filteredCreatures")
    public List<Object>  getFilteredCreatures(
            @RequestParam(required = false) String biome, @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "1") String distance) {
        return creatureService.getFilteredCreatures(biome, location, distance);
    }

    @GetMapping("/flow2")
    @CrossOrigin("http://minifiednd.com:8880")
    public List<Object> getFlow2(
            @RequestParam(required = false) String biome, @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer random) {
        return creatureService.getFlow2(biome, location, random);
    }
}