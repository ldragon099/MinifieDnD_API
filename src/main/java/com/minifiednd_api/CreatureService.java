package com.minifiednd_api;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;

import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getAllCreatures()
    {
        return query(
                "MATCH (creature:Creature)" +
                    "RETURN creature",
                Map.of()
        );
    }

    private List<Map<String, Object>> query(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            return session.readTransaction(
                    tx -> tx.run(query, params).list(r -> r.asMap(CreatureService::convert))
            );
        }
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
