package com.minifiednd_api.fixtures;

import com.minifiednd_api.models.Creature;

import java.util.*;

public class CreatureFixture {
    public static Creature getCreature() {
        return new Creature("Cat", "mm 320", "Tiny", "Unaligned", "0");
    }

    public static Object getCreatureAsObject() {
        Map<String, String> map;
        map = new HashMap<>();
        map.put("name", "Cat");
        map.put("source", "mm 320");
        map.put("size", "Tiny");
        map.put("alignment", "Unaligned");
        map.put("cr", "0");
        return map;
    }

    public static List<Object> getListOfCreaturesAsObjects(Integer length) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(getCreatureAsObject());
        }
        return list;
    }
}
