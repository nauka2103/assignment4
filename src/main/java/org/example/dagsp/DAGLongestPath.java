package org.example.dagsp;

import org.example.model.Graph;
import org.example.model.Edge;
import org.example.topo.KahnTopoSort;
import org.example.util.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * Алгоритм поиска самого длинного пути (Critical Path) в DAG.
 * Работает через динамическое программирование по топологическому порядку.
 */
public class DAGLongestPath {

    private final Graph g;
    private final Metrics metrics;
    private final double[] longest;
    private final int[] parent;

    public DAGLongestPath(Graph g, Metrics m, int source) {
        this.g = g;
        this.metrics = m;
        this.longest = new double[g.getV()];
        this.parent = new int[g.getV()];
        Arrays.fill(longest, Double.NEGATIVE_INFINITY);
        Arrays.fill(parent, -1);
        longest[source] = 0;
    }

    public void run(int src) {
        long start = System.nanoTime();

        // Топологическая сортировка
        KahnTopoSort topo = new KahnTopoSort(g, new Metrics());
        List<Integer> order = topo.sort();

        // Поиск длиннейших путей
        for (int u : order) {
            if (longest[u] != Double.NEGATIVE_INFINITY) {
                List<Edge> neighbors = g.getAdj().get(u);   // <-- getAdj() возвращает List<List<Edge>>
                for (Edge e : neighbors) {
                    int v = e.getV();       // или e.getTo() / e.getU() — см. Edge.java
                    double w = e.getW();    // или e.getWeight()
                    if (longest[u] + w > longest[v]) {
                        longest[v] = longest[u] + w;
                        parent[v] = u;
                    }
                }
            }
        }


        metrics.startTimer();
    }

    // --- Геттеры для тестов ---
    public double[] getLongestDist() {
        return longest;
    }

    public double getLongestDistanceTo(int v) {
        return longest[v];
    }

    public int[] getParent() {
        return parent;
    }
}
