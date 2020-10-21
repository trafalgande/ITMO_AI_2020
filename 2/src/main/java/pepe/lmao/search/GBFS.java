package pepe.lmao.search;

import pepe.lmao.model.City;
import pepe.lmao.model.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class GBFS {
    public GBFS(Graph graph, String start_, String finish_) {
        City start = graph.getGraph().entrySet().parallelStream()
                .filter(node -> node.getKey().getName().equals(start_))
                .findFirst()
                .orElseThrow().getKey();
        search(graph, start, finish_);
    }

    ArrayList<City> answer = new ArrayList<>();

    public void search(Graph graph, City start, String finish_) {
        PriorityQueue<City> queue = new PriorityQueue<>(graph.getGraph().size(), Comparator.comparingInt(n -> n.distance));
        queue.add(start);
        while (!queue.isEmpty()) {
            City current = queue.remove();
            graph.visit(current);
            answer.add(current);

            graph.findNeighbours(current)
                    .stream().filter(n -> !n.visited && !queue.contains(n) && graph.findNeighbours(n).stream().anyMatch(nn -> !nn.visited))
                    .forEach(queue::add);

            if (current.name.equals(finish_)) {
                printAnswer(answer);
                return;
            }
        }
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
