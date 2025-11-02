package org.example.model;

import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<Edge> edges;
    private final List<List<Edge>> adj;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, double w) {
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
        }
    }

    public int size() {
        return n;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<List<Edge>> getAdj() {
        return adj;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Integer> getNeighbors(int v) {
        List<Integer> list = new ArrayList<>();
        for (Edge e : adj.get(v)) {
            list.add(e.to);
        }
        return list;
    }

    public static Graph fromJSON(String json) {
        json = json.replaceAll("[\\n\\r\\t ]", "");
        boolean directed = json.contains("\"directed\":true");
        int n = Integer.parseInt(json.split("\"n\":")[1].split(",")[0]);
        Graph g = new Graph(n, directed);

        String edgesStr = json.split("\"edges\":\\[")[1].split("]")[0];
        String[] edgeEntries = edgesStr.split("\\},\\{");

        for (String e : edgeEntries) {
            e = e.replace("{", "").replace("}", "");
            String[] parts = e.split(",");
            int u = Integer.parseInt(parts[0].split(":")[1]);
            int v = Integer.parseInt(parts[1].split(":")[1]);
            double w = Double.parseDouble(parts[2].split(":")[1]);
            g.addEdge(u, v, w);
        }

        return g;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph (n=").append(n).append(", directed=").append(directed).append(")\n");
        for (Edge e : edges) sb.append(e).append("\n");
        return sb.toString();
    }
}
