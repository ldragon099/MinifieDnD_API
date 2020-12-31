package com.minifiednd_api.controllers;

import com.minifiednd_api.services.BiomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BiomeController {
    BiomeService biomeService = new BiomeService( "bolt://minifiednd.com:7687", "neo4j", "goblinMonkeyBaby");

    @GetMapping("/allBiomes")
    public List<Object> getAllBiomes() {
        return biomeService.getAllBiomes();
    }

    @GetMapping("/filteredBiomes")
    public List<Object>  getFilteredBiomes(
            @RequestParam(required = false) String creature, @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "1") String distance) {
        return biomeService.getFilteredBiomes(creature, location, distance);
    }
}
