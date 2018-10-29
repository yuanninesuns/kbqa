package org.horizon.Neo4j;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.apache.jena.base.Sys;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;

import static org.neo4j.driver.v1.Values.parameters;
//StatementResult result = session.run( "MATCH (a:YiItem) WHERE a.name = {name} " +
//                            "RETURN a.name AS name, a.type AS type",
//                    parameters( "name", "腹部透视" ) );
//            StatementResult result = session.run("MATCH p=()-[r:YiRELATION]->() RETURN p as name LIMIT 10");
//            StatementResult result = session.run("match(n:YiItem) where n.name={name}"+
//            "return n.name as name,n.type as type",
//                    parameters("name","腹部透视"));
public class Util {
    public static ArrayList<String> getResult(String name){
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "root"));
        ArrayList<String> arr = new ArrayList<>();
        arr.add(name);
        try (Session session = driver.session()) {
            StatementResult result = session.run("match(n{name:'感冒'})--(m:YiItem)" +
                    "return m.name as name,m.type as type,n.name,n.type limit 10");

            while (result.hasNext() )
            {
                Record record = result.next();
                arr.add(record.get("name").asString());
//                System.out.println(record+" "+record.get("name")+" "+record.get("type"));
            }
        }
        driver.close();
        return arr;
    }

    public static void main(String args[]){
        ArrayList<String> result = getResult("感冒");
        for(String arr : result){
            System.out.println(arr);
        }
    }
}
