package org.example;

import org.example.generator.GraphGenerator;
import org.example.model.Graph;
import org.example.model.Edge;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UtilityTests {

    @Test
    void testGraphGeneratorCreatesFiles() throws Exception {
        GraphGenerator.main(new String[]{});
        File dir = new File("data/generated");
        assertTrue(dir.exists() && Objects.requireNonNull(dir.listFiles()).length >= 9, "GraphGenerator should create 9 datasets");
    }

    @Test
    void testGraphBasicAddAndGet() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 5);

        List<Edge> edges = g.getEdges();
        assertEquals(2, edges.size());
        assertEquals(0, edges.get(0).getU());
        assertEquals(1, edges.get(0).getV());
    }
}
