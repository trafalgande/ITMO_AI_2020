package pepe.lmao;

import pepe.lmao.model.Graph;
import pepe.lmao.search.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();
        graph.init();

        String start_ = "Самара";
        String finish_ = "Ярославль";

        System.out.println("_Depth-First Search_ resulted in following route:");
        new DFS(graph, start_, finish_, null);
        graph.forget();

        System.out.println("_Breadth-First Search_ resulted in following route:");
        new BFS(graph, start_, finish_);
        graph.forget();

        System.out.println("_Depth-First Search With Bound_ resulted in following route:");
        new DFS(graph, start_, finish_, 9);
        graph.forget();

        System.out.println("_Iteration-Deepeing Depth-First Search_ resulted in following route:");
        for (int i = 1; i < graph.getGraph().size(); i++) {
            DFS dfs = new DFS(graph, start_, finish_, i);
            if (dfs.getAnswer().get(dfs.getAnswer().size() - 1).name.equals(finish_))
                break;
            else
                graph.forget();
        }
        graph.forget();

        System.out.println("_Bidirectional Search_ resulted in following route:");
        new BDS(graph, start_, finish_);
        graph.forget();

        System.out.println("_Greedy Best-First-Search_ resulted in following route:");
        new GBFS(graph, start_, finish_);
        graph.forget();

        System.out.println("_A* Search_ resulted in following route:");
        new AStar(graph, start_, finish_);
        graph.forget();
    }
}
