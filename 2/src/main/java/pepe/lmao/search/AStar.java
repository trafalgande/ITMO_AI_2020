package pepe.lmao.search;

import pepe.lmao.model.City;
import pepe.lmao.model.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AStar {
    public AStar(Graph graph, String start_, String finish_) {
        City start = graph.getGraph().entrySet().parallelStream()
                .filter(node -> node.getKey().getName().equals(start_))
                .findFirst()
                .orElseThrow().getKey();
        search(graph, start, finish_);
    }

    ArrayList<City> answer = new ArrayList<>();

    public void search(Graph graph, City start, String finish_) {
        PriorityQueue<City> open = new PriorityQueue<>(graph.getGraph().size(), Comparator.comparingInt(n -> n.distance));
        open.add(start);
        while (!open.isEmpty()) {
            City current = open.remove();
            graph.visit(current);
            answer.add(current);

            graph.findNeighbours(current)
                    .stream()
                    .filter(n -> !n.visited && !open.contains(n)
                            && graph.findNeighbours(n)
                            .stream().anyMatch(nn -> !nn.visited))
                    .min(Comparator.comparing(n -> n.distance))
                    .ifPresentOrElse(open::add,
                            () -> {
                                City parent = current.parent;
                                graph.findNeighbours(parent)
                                        .stream()
                                        .filter(n -> n != current && !n.visited && !open.contains(n)
                                                && graph.findNeighbours(n)
                                                .stream()
                                                .anyMatch(nn -> !nn.visited))
                                        .min(Comparator.comparing(n -> n.distance))
                                        .ifPresent(open::add);
                            });
            if (current.name.equals(finish_)) {
                printAnswer(answer);
                return;
            }

        }
        printAnswer(answer);
    }


    private void printAnswer(ArrayList<City> answer) {
        AtomicInteger distance = new AtomicInteger();
        StringBuilder sb = new StringBuilder();
        answer.forEach((city -> {
            sb.append(city.name).append(" -> ");
            distance.addAndGet(city.distance != null ? city.distance : 0);
        }));
        System.out.printf("{distance: %s} -- %s%n", distance.toString(), sb.substring(0, sb.length() - 4));
    }
}
