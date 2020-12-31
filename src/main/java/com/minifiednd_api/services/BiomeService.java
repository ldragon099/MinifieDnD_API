package com.minifiednd_api.services;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BiomeService implements AutoCloseable{
    private final Driver driver;

    public BiomeService( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public List<Object> getAllBiomes()
    {
        List<Map<String, Object>> result = query(
                "MATCH (biome:Biome)" +
                        "RETURN biome",
                Map.of()
        );

        return result.stream().map(BiomeService::extractPropertiesAsObject).collect(Collectors.toList());
    }

    public List<Object> getFilteredBiomes(String creature, String location, String distance) {


        List<Map<String, Object>> result = query(
                "",
                Map.of()
        );

        return result.stream().map(BiomeService::extractPropertiesAsObject).collect(Collectors.toList());
    }

    private List<Map<String, Object>> query(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            return session.readTransaction(
                    tx -> tx.run(query, params).list(r -> r.asMap(BiomeService::convert))
            );
        }
    }

    private static Object extractPropertiesAsObject(Map<String, Object> node) {
        return node.entrySet().iterator().next().getValue();
    }

    private static Object convert(Value value) {
        switch (value.type().name()) {
            case "PATH":
                return value.asList(BiomeService::convert);
            case "NODE":
            case "RELATIONSHIP":
                return value.asMap();
        }
        return value.asObject();
    }
}
