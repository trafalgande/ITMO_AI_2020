package pepe.lmao.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

@Data
public class Graph {
    private HashMap<String, HashMap<String, Integer>> map;
    private HashMap<City, LinkedList<City>> graph = new HashMap<>();

    @SneakyThrows
    public Graph() {
        init();
    }

    public void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        map = objectMapper.readValue(new File("src/main/data/distances.json"), new TypeReference<>() {
        });

        map.forEach((name, neighbours) -> addVertex(new City(null, name, null)));
        graph.forEach((city, neighbours) ->
                map.get(city.name).forEach((key, value) ->
                        addEdge(city, new City(city, key, value))));
    }

    public void addVertex(City vertex) {
        graph.putIfAbsent(vertex, new LinkedList<>());
    }

    public void addEdge(City source, City destination) {
        if (!graph.get(source).contains(destination))
            graph.get(source).add(destination);
    }

    public void printEdges() {
        for (City node : graph.keySet()) {
            System.out.print("The " + node.name + " has an edge towards: ");
            for (City neighbor : graph.get(node)) {
                System.out.print(neighbor.name + " ");
            }
            System.out.println();
        }
    }

    public Integer dist(City a, City b) {
        if (hasEdge(a, b))
            return
                    a.distance != null ? b.distance : 0;
        else
            return null;
    }

    public boolean hasEdge(City source, City destination) {
        return graph.containsKey(source)
                && graph.get(source).contains(destination);
    }

    public LinkedList<City> findNeighbours(City node) {
        return new LinkedList<>(graph.get(node));
    }

    public void forget() {
        graph.forEach((city, neighbours) -> {
            city.forget();
            neighbours.forEach(City::forget);
        });
    }

    public void forget(City node) {
        graph.keySet().forEach((city -> {
            if (city.name.equals(node.name)) city.forget();
        }));
    }

    public void visit(City current) {
        graph.forEach((city, neighbours) -> {
            if (city.name.equals(current.name)) {
                city.visited = true;
            }
            neighbours.forEach(node -> {
                if (node.name.equals(current.name)) {
                    node.visited = true;
                }
            });
        });
    }

}
