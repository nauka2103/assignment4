package org.example.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.DataSet;
import org.example.model.Edge;
import org.example.model.Graph;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GraphLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static DataSet loadGraph(String resourceName) {
        try {
            File file = new File(resourceName);
            if (!file.exists()) throw new RuntimeException("File not found: " + resourceName);
            JsonNode root = mapper.readTree(file);

            boolean directed = root.path("directed").asBoolean(true);
            int n = root.path("n").asInt(0);
            Graph g = new Graph(n, directed);

            List<Edge> edges = mapper.convertValue(root.path("edges"), new TypeReference<List<Edge>>() {});
            if (edges != null) {
                for (Edge e : edges) {
                    g.addEdge(e.getU(), e.getV(), e.getW());
                }
            }

            int source = root.path("source").asInt(0);
            String wm = root.path("weight_model").asText("edge");
            return new DataSet(g, source, wm, resourceName);
        } catch (IOException ex) {
            throw new RuntimeException("File load error: " + resourceName, ex);
        }
    }
}
