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

/**
 * GraphLoader — отвечает за чтение графов из JSON-файлов.
 * Поддерживает поля:
 *   - directed (boolean)
 *   - n (int)
 *   - edges (список u, v, w)
 *   - source (int)
 *   - weight_model (String)
 */
public class GraphLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Загружает граф из JSON-файла.
     * @param resourceName путь к файлу (например, "data/graph_small_1.json")
     * @return объект DataSet с графом, source и моделью весов
     */
    public static DataSet loadGraph(String resourceName) {
        try {
            File file = new File(resourceName);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + resourceName);
            }

            JsonNode root = mapper.readTree(file);

            // --- Step 1: Считываем параметры графа ---
            boolean directed = root.path("directed").asBoolean(false);
            int n = root.path("n").asInt(0);
            Graph g = new Graph(n, directed);

            // --- Step 2: Считываем список рёбер ---
            List<Edge> edges = mapper.convertValue(
                    root.path("edges"),
                    new TypeReference<List<Edge>>() {}
            );
            for (Edge e : edges) {
                g.addEdge(e.getU(), e.getV(), e.getW());
            }

            // --- Step 3: Дополнительные параметры ---
            int source = root.path("source").asInt(0);
            String wm = root.path("weight_model").asText("edge");

            // --- Step 4: Возвращаем результат ---
            return new DataSet(g, source, wm, resourceName);

        } catch (IOException e) {
            throw new RuntimeException("File load error: " + resourceName, e);
        }
    }
}
