package org.example.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GraphGenerator {

    private static final Random random = new Random();

    static class Edge {
        int u;
        int v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    static class GraphData {
        boolean directed = true;
        int n;
        List<Edge> edges;
        int source;
        String weight_model = "edge";
    }

    public static void main(String[] args) {
        String[] sizes = {"small", "medium", "large"};
        int[] counts = {3, 3, 3};
        int[] nodes = {8, 15, 30};
        int[] edges = {10, 25, 60};

        File dataDir = new File("src/main/resources/data");
        if (!dataDir.exists()) {
            boolean mkdirs = dataDir.mkdirs();
        }

        for (int s = 0; s < sizes.length; s++) {
            for (int i = 1; i <= counts[s]; i++) {
                String filename = String.format("%s_%d.json", sizes[s], i);
                GraphData graph = generateGraph(nodes[s], edges[s], i % 2 == 0);
                saveGraph(graph, new File(dataDir, filename));
                System.out.println("✅ Created " + filename);
            }
        }

        System.out.println("\nAll datasets successfully generated in /data/");
    }


    private static GraphData generateGraph(int n, int m, boolean cyclic) {
        GraphData g = new GraphData();
        g.n = n;
        g.edges = new ArrayList<>();
        g.source = 0;

        Set<String> used = new HashSet<>();

        for (int i = 0; i < n - 1; i++) {
            int w = random.nextInt(9) + 1;
            g.edges.add(new Edge(i, i + 1, w));
            used.add(i + "-" + (i + 1));
        }

        while (g.edges.size() < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u == v) continue;
            String key = u + "-" + v;
            if (used.contains(key)) continue;
            used.add(key);
            int w = random.nextInt(9) + 1;
            g.edges.add(new Edge(u, v, w));
        }

        if (cyclic) {
            int cycles = Math.max(1, n / 5);
            for (int i = 0; i < cycles; i++) {
                int a = random.nextInt(n);
                int b = random.nextInt(n);
                if (a != b) {
                    g.edges.add(new Edge(b, a, random.nextInt(5) + 1));
                }
            }
        }

        return g;
    }

    private static void saveGraph(GraphData g, File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(g, writer);
        } catch (IOException e) {
            System.err.println("❌ Error writing " + file.getName() + ": " + e.getMessage());
        }
    }
}
