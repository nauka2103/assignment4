package org.example.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.example.model.Edge;
import org.example.model.Graph;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GraphGenerator {

    public static void main(String[] args) throws IOException {
        int[] vertexCounts = {5, 10, 15, 20, 25, 30, 40, 50, 60};
        ObjectMapper mapper = new ObjectMapper();
        Random rand = new Random();

        File dir = new File("data/generated");
        if (!dir.exists()) dir.mkdirs();

        for (int i = 0; i < vertexCounts.length; i++) {
            int V = vertexCounts[i];
            Graph g = new Graph(V, true);

            int E = rand.nextInt(V * 3) + V;

            for (int j = 0; j < E; j++) {
                int u = rand.nextInt(V);
                int v = rand.nextInt(V);
                if (u == v) continue;
                int w = rand.nextInt(50) + 1;
                g.addEdge(u, v, w);
            }

            int source = rand.nextInt(V);
            String weightModel = "edge";

            ObjectNode root = mapper.createObjectNode();
            root.put("V", V);

            ArrayNode edgesArray = mapper.createArrayNode();
            for (Edge e : g.getEdges()) {
                ObjectNode edgeNode = mapper.createObjectNode();
                edgeNode.put("u", e.getU());
                edgeNode.put("v", e.getV());
                edgeNode.put("w", e.getW());
                edgesArray.add(edgeNode);
            }

            root.set("edges", edgesArray);
            root.put("source", source);
            root.put("weight_model", weightModel);

            String fileName = String.format("graph_%02d.json", i + 1);
            File file = new File(dir, fileName);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            System.out.println("✅ Created " + file.getName() + " (V=" + V + ", source=" + source + ")");
        }
    }
}
