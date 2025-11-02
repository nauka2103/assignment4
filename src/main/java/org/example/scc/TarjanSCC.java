package org.example.scc;

import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;

import java.util.*;

/**
 * Tarjan SCC. After run() you can call getComponents() and getCompIndex().
 */
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;

    private final int n;
    private final int[] disc;
    private final int[] low;
    private final boolean[] onStack;
    private final Deque<Integer> stack;
    private final List<List<Integer>> components;
    private final int[] compIndex;
    private int time = 0;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.n = graph.getV();
        this.disc = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new ArrayDeque<>();
        this.components = new ArrayList<>();
        this.compIndex = new int[n];
        Arrays.fill(disc, -1);
        Arrays.fill(compIndex, -1);
    }

    public void run() {
        metrics.startTimer();
        for (int i = 0; i < n; i++) if (disc[i] == -1) dfs(i);
        metrics.stopTimer();
    }

    private void dfs(int u) {
        metrics.incOps();
        disc[u] = low[u] = ++time;
        stack.push(u);
        onStack[u] = true;

        for (Edge e : graph.getAdj().get(u)) {
            int v = e.getV();
            metrics.incOps();
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                compIndex[v] = components.size();
                comp.add(v);
                if (v == u) break;
            }
            components.add(comp);
        }
    }

    public List<List<Integer>> getComponents() { return components; }
    public int[] getCompIndex() { return compIndex; }

    public Graph buildCondensation() {
        int c = components.size();
        Graph dag = new Graph(c, true);
        Set<String> seen = new HashSet<>();
        for (Edge e : graph.getEdges()) {
            int a = compIndex[e.getU()];
            int b = compIndex[e.getV()];
            if (a != b) {
                String key = a + "-" + b;
                if (seen.add(key)) dag.addEdge(a, b, e.getW());
            }
        }
        return dag;
    }
}
