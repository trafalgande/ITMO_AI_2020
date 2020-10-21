package pepe.lmao.search;

import pepe.lmao.model.City;
import pepe.lmao.model.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BDS {
    public BDS(Graph graph, String start_, String finish_) {
        City start = graph.getGraph().entrySet().parallelStream()
                .filter(node -> node.getKey().getName().equals(start_))
                .findFirst()
                .orElseThrow().getKey();
        City finish = graph.getGraph().entrySet().parallelStream()
                .filter(node -> node.getKey().getName().equals(finish_))
                .findFirst()
                .orElseThrow().getKey();
        search(graph, start, finish);
    }

    ArrayList<City> answerLeft = new ArrayList<>();
    ArrayList<City> answerRight = new ArrayList<>();

    public void search(Graph graph, City start, City finish) {
        Queue<City> queueLeft = new ArrayDeque<>();
        Queue<City> queueRight = new ArrayDeque<>();

        queueLeft.add(start);
        queueRight.add(finish);
        int depthLeft = 0;
        int depthRight = 0;
        AtomicReference<AtomicInteger> counterLeft = new AtomicReference<>(new AtomicInteger());
        AtomicReference<AtomicInteger> counterRight = new AtomicReference<>(new AtomicInteger());
        while (!queueLeft.isEmpty() && !queueRight.isEmpty()) {
            depthLeft = getDepth(graph, queueLeft, depthLeft, counterLeft, answerLeft);
            depthRight = getDepth(graph, queueRight, depthRight, counterRight, answerRight);

            City intersection;
            if ((intersection = isIntersected(answerLeft, answerRight)) != null) {

                answerLeft = new ArrayList<>(answerLeft.subList(0, answerLeft.indexOf(intersection) + 1));
                answerRight = new ArrayList<>(answerRight.subList(0, answerRight.indexOf(intersection)));
                printAnswer(answerLeft, answerRight, depthLeft + depthRight + 1);
                return;
            }
        }
    }

    private int getDepth(Graph graph, Queue<City> queueRight, int depthRight,
                              AtomicReference<AtomicInteger> counterRight, ArrayList<City> answerRight) {
        City right = queueRight.remove();
        graph.visit(right);
        answerRight.add(right);

        if (counterRight.get().get() != 0) {
            counterRight.get().set(counterRight.get().decrementAndGet());
        }
        if (counterRight.get().get() == 0)
            depthRight++;

        graph.findNeighbours(right)
                .stream().filter(n -> !n.visited)
                .forEach((neighbour) -> {
                    queueRight.add(neighbour);
                    counterRight.get().getAndIncrement();

                });
        return depthRight;
    }

    private void printAnswer(ArrayList<City> answerLeft, ArrayList<City> answerRight, int depth) {
        AtomicInteger distance = new AtomicInteger();
        StringBuilder sb = new StringBuilder();

        answerLeft.forEach((city -> {
            sb.append(city.name).append(" -> ");
            distance.addAndGet(city.distance != null ? city.distance : 0);
        }));

        for (int i = answerRight.size()-1; i >= 0; i--) {
            sb.append(answerRight.get(i).name).append(" -> ");
            distance.addAndGet(answerRight.get(i).distance != null ? answerRight.get(i).distance : 0);
        }

        System.out.printf("{depth: %d, distance: %s} -- %s%n", depth, distance.toString(), sb.substring(0, sb.length() - 4));
    }

    private City isIntersected(ArrayList<City> answerLeft, ArrayList<City> answerRight) {
        for (City city : answerLeft) {
            for (City value : answerRight) {
                if (city.name.equals(value.name))
                    return city;
            }
        }
        return null;
    }
}
