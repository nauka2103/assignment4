package org.example.dagsp;

import org.example.model.Graph;
import org.example.util.Metrics;
import org.example.topo.KahnTopoSort;

import java.util.List;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    private double[] dist;
    private int[] parent;

    public DAGShortestPath(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public void run(int source) {
        int n = graph.getV();
        dist = new double[n];
        parent = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        dist[source] = 0;

        // топологический порядок
        KahnTopoSort topo = new KahnTopoSort(graph, metrics);
        List<Integer> order = topo.sort();

        // динамика по топо-порядку
        for (int u : order) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (var e : graph.getAdj().get(u)) {
                    int v = e.getU();
                    double w = e.getW();
                    metrics.relaxations++;
                    if (dist[v] > dist[u] + w) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                    }
                }
            }
        }
    }

    public double[] getDist() {
        return dist;
    }

    public int[] getParent() {
        return parent;
    }

    public double getDistanceTo(int v) {
        return dist[v];
    }
}
