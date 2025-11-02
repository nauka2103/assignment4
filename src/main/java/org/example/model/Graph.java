package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple directed weighted graph.
 */
public class Graph {
    private final int v;
    private final boolean directed;
    private final List<Edge> edges;
    private final List<List<Edge>> adj;

    public Graph(int v, boolean directed) {
        this.v = v;
        this.directed = directed;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>(v);
        for (int i = 0; i < v; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int vv, double w) {
        Edge e = new Edge(u, vv, w);
        edges.add(e);
        adj.get(u).add(e);
        if (!directed) {
            Edge back = new Edge(vv, u, w);
            adj.get(vv).add(back);
            edges.add(back);
        }
    }

    public int getV() { return v; }
    public boolean isDirected() { return directed; }
    public List<Edge> getEdges() { return edges; }
    public List<List<Edge>> getAdj() { return adj; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph(v=").append(v).append(", directed=").append(directed).append(")\n");
        for (Edge e : edges) sb.append("  ").append(e).append("\n");
        return sb.toString();
    }
}
