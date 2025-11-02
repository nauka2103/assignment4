package org.example;

import org.example.io.GraphLoader;
import org.example.io.SCCCsvWriter;
import org.example.scc.TarjanSCC;
import org.example.topo.KahnTopoSort;
import org.example.dagsp.DAGShortestPath;
import org.example.dagsp.DAGLongestPath;
import org.example.model.DataSet;
import org.example.model.Graph;
import org.example.util.Metrics;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : "data/small_1.json";
        DataSet ds = GraphLoader.loadGraph(path);
        Graph g = ds.getGraph();

        // === SCC ===
        Metrics mScc = new Metrics();
        TarjanSCC scc = new TarjanSCC(g, mScc);
        scc.run();
        System.out.println("SCC count = " + scc.getComponents().size());
        SCCCsvWriter.write("results/scc.csv", g, scc, mScc);

        // === Condensation + Topological sort ===
        Graph dag = scc.buildCondensation();
        Metrics mTopo = new Metrics();
        KahnTopoSort topo = new KahnTopoSort(dag, mTopo);
        List<Integer> order = topo.sort();
        System.out.println("Topo order = " + Arrays.toString(order.toArray()));
        System.out.println("Topo time ms = " + mTopo.getTimeMs());

        // === DAG Shortest & Longest Paths ===
        Metrics ms = new Metrics();
        Metrics ml = new Metrics();

        DAGShortestPath sp = new DAGShortestPath(dag, ms);
        DAGLongestPath lp = new DAGLongestPath(dag, ml, 0);

        int src = Math.min(ds.getSource(), dag.getV() - 1);

        sp.run(src);
        lp.run(src);

        System.out.println("Shortest distances = " + Arrays.toString(sp.getDist()));
        System.out.println("Longest distances = " + Arrays.toString(lp.getLongestDist()));

        System.out.println("Shortest path metrics: " + ms);
        System.out.println("Longest path metrics: " + ml);
    }
}
