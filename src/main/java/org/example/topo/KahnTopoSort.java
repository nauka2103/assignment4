package org.example.topo;

import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;

import java.util.*;


public class KahnTopoSort {
    private final Graph graph;
    private final Metrics metrics;

    public KahnTopoSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<Integer> sort() {
        metrics.startTimer();
        int n = graph.getV();
        int[] indeg = new int[n];
        for (Edge e : graph.getEdges()) {
            indeg[e.getV()]++;
            metrics.incOps();
        }

        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) { q.add(i); metrics.incOps(); }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.remove();
            order.add(u);
            metrics.incOps();
            for (Edge e : graph.getAdj().get(u)) {
                int v = e.getV();
                indeg[v]--;
                metrics.incOps();
                if (indeg[v] == 0) { q.add(v); metrics.incOps(); }
            }
        }

        metrics.stopTimer();
        return order;
    }
}
