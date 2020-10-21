package pepe.lmao.search;

import lombok.Data;
import pepe.lmao.model.City;
import pepe.lmao.model.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Data
public class BFS {
    public BFS(Graph graph, String start_, String finish_) {
        City start = graph.getGraph().entrySet().parallelStream()
                .filter(node -> node.getKey().getName().equals(start_))
                .findFirst()
                .orElseThrow().getKey();
        search(graph, start, finish_);
    }

    ArrayList<City> answer = new ArrayList<>();

    public void search(Graph graph, City start, String finish_) {
        Queue<City> queue = new ArrayDeque<>();
        queue.add(start);
        int depth = 0;
        AtomicReference<AtomicInteger> counter = new AtomicReference<>(new AtomicInteger());
        while (!queue.isEmpty()) {
            City current = queue.remove();
            if (current.visited)
                continue;
            else
                graph.visit(current);
            answer.add(current);
            if (counter.get().get() != 0) {
                counter.get().set(counter.get().decrementAndGet());
            }
            if (counter.get().get() == 0)
                depth++;
            counter.set(new AtomicInteger());

            graph.findNeighbours(current)
                    .stream().filter(n -> !n.visited)
                    .forEach((neighbour) -> {
                        queue.add(neighbour);
                        counter.get().getAndIncrement();

                    });

            if (current.name.equals(finish_)) {
                printAnswer(answer, depth);
                return;
            }
        }
    }

    private void printAnswer(ArrayList<City> answer, int depth) {
        AtomicInteger distance = new AtomicInteger();
        StringBuilder sb = new StringBuilder();
        answer.forEach((city -> {
            sb.append(city.name).append(" -> ");
            distance.addAndGet(city.distance != null ? city.distance : 0);
        }));
        System.out.printf("{depth: %d, distance: %s} -- %s%n", depth, distance.toString(), sb.substring(0, sb.length() - 4));
    }
}
