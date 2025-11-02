package org.example.dagsp;

import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;

import java.util.Arrays;
import java.util.List;

/** Longest path (critical) on DAG. */
public class DAGLongestPath {
    private final Graph dag;
    private final Metrics metrics;

    public DAGLongestPath(Graph dag, Metrics metrics) { this.dag = dag; this.metrics = metrics; }

    public double[] run(int src, List<Integer> topo) {
        int n = dag.getV();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[src] = 0.0;
        metrics.startTimer();
        for (int u : topo) {
            if (dist[u] == Double.NEGATIVE_INFINITY) continue;
            for (Edge e : dag.getAdj().get(u)) {
                metrics.incOps();
                int v = e.getV();
                double w = e.getW();
                if (dist[v] < dist[u] + w) dist[v] = dist[u] + w;
            }
        }
        metrics.stopTimer();
        return dist;
    }
}
