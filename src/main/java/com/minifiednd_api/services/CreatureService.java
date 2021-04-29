package com.minifiednd_api.services;

import com.minifiednd_api.models.Creature;
import com.minifiednd_api.models.CreatureSet;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreatureService implements AutoCloseable{
    private final Driver driver;

    public CreatureService( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public List<Object> getAllCreatures()
    {
        List<Map<String, Object>> result = query(
                "MATCH (creature:Creature)" +
                    "RETURN creature",
                Map.of()
        );

        return result.stream().map(CreatureService::extractPropertiesAsObject).collect(Collectors.toList());
    }

    public CreatureSet getFilteredCreatures(String biome, String location, Integer random) {
        String locationBiomeFilter = "(c1:Creature)-[:LIVES_IN]->(location:Location {name: $location})-[:IS_IN]->(biome:Biome {name: $biome})" +
                " RETURN c1 AS creature";
        String locationFilter = "(c2:Creature)-[:LIVES_IN]->(location:Location {name: $location})" +
                " RETURN c2 AS creature";
        String biomeFilter = "(c3:Creature)-[:LIVES_IN]->(biome:Biome {name: $biome})" +
                " RETURN c3 AS creature";
        List<Map<String, Object>> locationBiomeResponse = query("MATCH " + locationBiomeFilter,
                    Map.of("location", location, "biome", biome));
            List<Object> locationBiomeList = locationBiomeResponse.stream().map(CreatureService::extractPropertiesAsObject).collect(Collectors.toList());
            locationBiomeList = RandomSubset(locationBiomeList, (int) (random*(0.4)));
            List<Map<String, Object>> locationResponse = query("MATCH " + locationFilter,
                    Map.of("location", location));
            List<Object> locationList = locationResponse.stream().map(CreatureService::extractPropertiesAsObject).collect(Collectors.toList());
            locationList = RandomSubset(locationList, (int) (random*(0.3)));
            List<Map<String, Object>> biomeResponse = query("MATCH " + biomeFilter,
                    Map.of("biome", biome));
            List<Object> biomeList = biomeResponse.stream().map(CreatureService::extractPropertiesAsObject).collect(Collectors.toList());
            biomeList = RandomSubset(biomeList, (int) (random*(0.2)));
            return new CreatureSet(
                    ConvertObjectListToCreatureList(locationBiomeList),
                    ConvertObjectListToCreatureList(locationList),
                    ConvertObjectListToCreatureList(biomeList)
                    );
    }

    public List<Creature> getFlow2(String biome, String location, Integer random) {
        String locationBiomeFilter = "(c1:Creature)-[:LIVES_IN]->(location:Location {name: $location})-[:IS_IN]->(biome:Biome {name: $biome})" +
                " RETURN c1 AS creature";
        String locationFilter = "(c2:Creature)-[:LIVES_IN]->(location:Location {name: $location})" +
                " RETURN c2 AS creature";
        String biomeFilter = "(c3:Creature)-[:LIVES_IN]->(biome:Biome {name: $biome})" +
                " RETURN c3 AS creature";

        List<Map<String, Object>> result;
        if(biome == null || biome.isBlank()) {
            result = query("MATCH (biome:Biome) RETURN biome", Map.of());
        }
        else if(location == null || location.isBlank()) {
            result = query("MATCH (location:Location)-[IS_IN]->(:Biome {name: $biome}) RETURN location", Map.of("biome", biome));
        }
        else {
            result = query("MATCH " + locationBiomeFilter + " UNION MATCH " + locationFilter + " UNION MATCH " + biomeFilter,
                    Map.of("location", location, "biome", biome));
        }

        List<Object> list = result.stream().map(CreatureService::extractPropertiesAsObject).collect(Collectors.toList());

        list = RandomSubset(list, random);
        List<Creature> creatureList = ConvertObjectListToCreatureList(list);

        return creatureList;
    }

    public static List<Object> RandomSubset(List<Object> list, Integer subsetLength) {
        if(subsetLength != null && subsetLength <= list.size()) {
            Collections.shuffle(list);
            return list.subList(0, subsetLength);
        }
        return list;
    }

    public static List<Creature> ConvertObjectListToCreatureList(List<Object> list) {
        List<Creature> creatureList = new ArrayList<>();
        for (Object object: list) {
            @SuppressWarnings("unchecked")
            Map<String,String> creatureMap = (Map<String,String>)object;
            creatureList.add(new Creature(creatureMap.get("name"), creatureMap.get("source"),creatureMap.get("size"),creatureMap.get("alignment"), creatureMap.get("cr")));
        }

        return creatureList;
    }

    private List<Map<String, Object>> query(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            return session.readTransaction(
                    tx -> tx.run(query, params).list(r -> r.asMap(CreatureService::convert))
            );
        }
    }

    public static Object extractPropertiesAsObject(Map<String, Object> node) {
        return node.entrySet().iterator().next().getValue();
    }

    private static Object convert(Value value) {
        switch (value.type().name()) {
            case "PATH":
                return value.asList(CreatureService::convert);
            case "NODE":
            case "RELATIONSHIP":
                return value.asMap();
        }
        return value.asObject();
    }
}
