package org.example.scc;

import org.example.model.Graph;
import org.example.model.Edge;
import org.example.util.Metrics;

import java.util.*;


public class TarjanSCC {

    private Graph graph;
    private Metrics metrics;

    private int time;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;
    private int[] compIndex; // component index of each vertex

    public TarjanSCC(Graph g, Metrics m) {
        this.graph = g;
        this.metrics = m;
        int n = g.size();
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        compIndex = new int[n];
        Arrays.fill(disc, -1);
    }

    public List<List<Integer>> run() {
        metrics.start();
        for (int i = 0; i < graph.size(); i++) {
            if (disc[i] == -1) {
                dfs(i);
            }
        }
        metrics.stop();
        return components;
    }

    private void dfs(int u) {
        metrics.incDFS();
        disc[u] = low[u] = ++time;
        stack.push(u);
        onStack[u] = true;

        for (Edge e : graph.getAdj().get(u)) {
            int v = e.to;
            metrics.incEdge();
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
                comp.add(v);
                compIndex[v] = components.size();
                if (v == u) break;
            }
            components.add(comp);
        }
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int[] getCompIndex() {
        return compIndex;
    }


    public Graph buildCondensationDAG() {
        int c = components.size();
        Graph dag = new Graph(c, true);
        Set<String> added = new HashSet<>();

        for (Edge e : graph.getEdges()) {
            int a = compIndex[e.from];
            int b = compIndex[e.to];
            if (a != b) {
                String key = a + "->" + b;
                if (!added.contains(key)) {
                    dag.addEdge(a, b, e.weight);
                    added.add(key);
                }
            }
        }

        return dag;
    }
}
