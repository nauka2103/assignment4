package org.example;

import org.example.scc.TarjanSCC;
import org.example.topo.KahnTopoSort;
import org.example.model.Graph;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTests {

    @Test
    void testTarjanSCC_DetectsTwoComponents() {
        Graph g = new Graph(6, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);

        Metrics m = new Metrics();
        TarjanSCC scc = new TarjanSCC(g, m);
        scc.run();

        assertEquals(2, scc.getComponents().size(), "Should detect 2 SCCs");
    }

    @Test
    void testKahnTopoSort_ValidOrder() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);

        Metrics m = new Metrics();
        KahnTopoSort topo = new KahnTopoSort(g, m);
        List<Integer> order = topo.sort();

        assertEquals(5, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(4), "Topological order should respect dependencies");
    }
}
