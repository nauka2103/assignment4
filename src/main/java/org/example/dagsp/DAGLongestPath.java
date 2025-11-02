package org.example.dagsp;

import org.example.model.Graph;
import org.example.model.Edge;
import org.example.topo.KahnTopoSort;
import org.example.util.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * Находит самые длинные пути (critical path) в DAG.
 * Использует динамическое программирование по топологическому порядку.
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

    public void run(int source) {
        long start = System.nanoTime();

        // Топологическая сортировка
        KahnTopoSort topo = new KahnTopoSort(g, new Metrics());
        List<Integer> order = topo.sort();

        // Поиск длиннейших путей в DAG
        for (int u : order) {
            if (longest[u] != Double.NEGATIVE_INFINITY) {
                for (Edge e : g.getAdj(u)) { // получаем список рёбер из u
                    int v = g.getV();
                    double w = e.getW();
                    if (longest[u] + w > longest[v]) {
                        longest[v] = longest[u] + w;
                        parent[v] = u;
                    }
                }
            }
        }

        metrics.getTimeNs((System.nanoTime() - start) / 1_000_000.0);
    }

    // --- Геттеры для тестов и вывода ---
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
