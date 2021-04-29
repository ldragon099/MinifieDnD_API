package com.minifiednd_api.models;

import java.util.List;

public class CreatureSet {
    private final List<Creature> locationBiomeMatch;
    private final List<Creature> locationMatch;
    private final List<Creature> biomeMatch;

    public CreatureSet(List<Creature> locationBiomeMatch, List<Creature> locationMatch, List<Creature> biomeMatch) {
        this.locationBiomeMatch = locationBiomeMatch;
        this.locationMatch = locationMatch;
        this.biomeMatch = biomeMatch;
    }

    public List<Creature> getLocationBiomeMatch() {
        return locationBiomeMatch;
    }
    public List<Creature> getLocationMatch() {
        return locationMatch;
    }
    public List<Creature> getBiomeMatch() {
        return biomeMatch;
    }
}
