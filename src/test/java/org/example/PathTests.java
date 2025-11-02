package org.example;

import org.example.dagsp.DAGShortestPath;
import org.example.dagsp.DAGLongestPath;
import org.example.model.Graph;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PathTests {

    @Test
    void testDAGShortestPath() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 3, 6);
        g.addEdge(3, 4, 1);
        g.addEdge(2, 4, 4);

        Metrics m = new Metrics();
        DAGShortestPath sp = new DAGShortestPath(g, m);
        sp.run(0);

        double[] dist = sp.getDist();
        assertEquals(5, dist.length);
        assertTrue(dist[4] > 0, "Distance to node 4 should be positive");

        // 🔹 Проверка методов getDistanceTo и getParent
        assertTrue(sp.getDistanceTo(4) > 0, "getDistanceTo(4) should return positive distance");
        int[] parent = sp.getParent();
        assertNotNull(parent, "Parent array should not be null");
    }

    @Test
    void testDAGLongestPath() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 3, 6);
        g.addEdge(3, 4, 1);
        g.addEdge(2, 4, 4);

        Metrics m = new Metrics();
        DAGLongestPath lp = new DAGLongestPath(g, m, 0);
        lp.run(0);

        // 🔹 Проверяем getLongestDist()
        double[] longest = lp.getLongestDist();
        assertEquals(5, longest.length);
        System.out.println("🔹 Longest distances = " + Arrays.toString(longest));
        System.out.println("🔹 Parent array = " + Arrays.toString(lp.getParent()));

        assertTrue(longest[4]  > 0, "Longest distance to node 4 should be positive");

        //  Проверяем getLongestDistanceTo() и getParent()
        assertTrue(lp.getLongestDistanceTo(4) > 0, "getLongestDistanceTo(4) should be >= 6");
        int[] parent = lp.getParent();
        assertNotNull(parent, "Parent array in LongestPath should not be null");
    }
}
