package pepe.lmao.search;

import lombok.Data;
import pepe.lmao.model.City;
import pepe.lmao.model.Graph;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class DFS {
    public DFS(Graph graph, String start_, String finish_, Integer bound) {
        City start = graph.getGraph().entrySet().parallelStream()
                .filter(node -> node.getKey().getName().equals(start_))
                .findFirst()
                .orElseThrow().getKey();
        search(graph, start, finish_, bound);
    }

    ArrayList<City> answer = new ArrayList<>();

    public void search(Graph graph, City start, String finish_, Integer bound) {
        Stack<City> stack = new Stack<>();
        stack.push(start);
        int depth = 0;
        while (!stack.isEmpty() || (bound != null && depth <= bound)) {
            City current;
            if (bound != null && depth == bound) {
                current = stack.pop();
                if (answer.stream().anyMatch(s -> s.name.equals(current.parent.name))) {
                    depth = answer.indexOf(answer.stream()
                            .filter(s -> s.name.equals(current.parent.name))
                            .findFirst()
                            .get());
                    answer = new ArrayList<>(answer.subList(0, depth));
                }
            } else {
                current = stack.pop();
            }

            graph.visit(current);
            answer.add(current);
            depth++;

            graph.findNeighbours(current)
                    .stream().filter(n -> !n.visited)
                    .forEach(stack::push);

            if (current.name.equals(finish_)) {
                printAnswer(answer, depth, bound);
                return;
            } else if (bound != null && depth > bound) {
                return;
            }
        }


    }

    private void printAnswer(ArrayList<City> answer, int depth, Integer bound) {
        AtomicInteger distance = new AtomicInteger();
        StringBuilder sb = new StringBuilder();
        answer.forEach((city -> {
            sb.append(city.name).append(" -> ");
            distance.addAndGet(city.distance != null ? city.distance : 0);
        }));
        if (bound != null)
            System.out.printf("{depth: %d, distance: %s, bound: %s} -- %s%n", depth, distance.toString(), bound, sb.substring(0, sb.length() - 4));
        else
        System.out.printf("{depth: %d, distance: %s} -- %s%n", depth, distance.toString(), sb.substring(0, sb.length() - 4));
    }

}
