package com.minifiednd_api.services;

import com.minifiednd_api.models.Creature;
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

    public List<Object> getFilteredCreatures(String biome, String location, String distance) {
//        String locationBiomeFilter = "(c1:Creature)-[:LIVES_IN]->(location:Location {name: \"%1$s\"})-[:IS_IN]->(biome:Biome {name: \"%2$s\"})" +
//                                     " RETURN c1 AS creature";
//        String locationFilter = "(c2:Creature)-[:LIVES_IN]->(location:Location {name: \"%1$s\"})" +
//                                " RETURN c2 AS creature";
//        String biomeFilter = "(c3:Creature)-[:LIVES_IN]->(biome:Biome {name: \"%1$s\"})" +
//                             " RETURN c3 AS creature";
        String locationBiomeFilter = "(c1:Creature)-[:LIVES_IN]->(location:Location {name: $location})-[:IS_IN]->(biome:Biome {name: $biome})" +
                " RETURN c1 AS creature";
        String locationFilter = "(c2:Creature)-[:LIVES_IN]->(location:Location {name: $location})" +
                " RETURN c2 AS creature";
        String biomeFilter = "(c3:Creature)-[:LIVES_IN]->(biome:Biome {name: $biome})" +
                " RETURN c3 AS creature";

//        String query = "MATCH ";
//        final String union = " UNION MATCH ";
//        if(location != null && !location.isBlank()) {
//            if(biome != null && !biome.isBlank()) {
//                query = String.format(query.concat(locationBiomeFilter), location, biome);
//                query = String.format(query.concat(union + biomeFilter + union), biome);
//            }
//            query = String.format(query.concat(locationFilter), location);
//        }
//        else if(biome != null && !biome.isBlank()) {
//            query = String.format(query.concat(biomeFilter), biome);
//        }
//        else {
//            return getAllCreatures();
//        }
        List<Map<String, Object>> result;
        if(location != null && !location.isBlank()) {
            if(biome != null && !biome.isBlank()) {
                result = query("MATCH " + locationBiomeFilter + " UNION MATCH " + locationFilter + " UNION MATCH " + biomeFilter,
                        Map.of("location", location, "biome", biome));
            }
            else {
                result = query("MATCH " + locationFilter,
                        Map.of("location", location));
            }
        }
        else if(biome != null && !biome.isBlank()) {
            result = query("MATCH " + biomeFilter,
                    Map.of("biome", biome));
        }
        else {
            return getAllCreatures();
        }

        return result.stream().map(CreatureService::extractPropertiesAsObject).collect(Collectors.toList());
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
