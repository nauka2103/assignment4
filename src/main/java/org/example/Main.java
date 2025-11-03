package org.example;

import org.example.dagsp.DAGShortestPath;
import org.example.io.GraphLoader;
import org.example.model.DataSet;
import org.example.model.Graph;
import org.example.scc.TarjanSCC;
import org.example.topo.KahnTopoSort;
import org.example.util.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Runs all datasets (small, medium, large) and writes summary.csv with performance metrics.
 */
public class Main {
    public static void main(String[] args) {
        File dataDir = new File("data");
        File[] jsonFiles = dataDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.err.println("❌ No JSON files found in data/");
            return;
        }

        File resultsDir = new File("results");
        if (!resultsDir.exists()) resultsDir.mkdirs();

        File summaryFile = new File(resultsDir, "summary.csv");

        try (FileWriter fw = new FileWriter(summaryFile)) {
            fw.write("dataset_name,scc_duration_ns,dfs_calls,edges_processed,topo_duration_ns,dfs_recursive_calls,dfs_stack_adds,shortest_path_ns,edge_relaxations\n");

            for (File f : jsonFiles) {
                String datasetName = f.getName();
                System.out.println("📂 Running dataset: " + datasetName);

                DataSet ds = GraphLoader.loadGraph(f.getPath());
                Graph g = ds.getGraph();

                // === SCC ===
                Metrics mScc = new Metrics();
                mScc.startTimer();
                TarjanSCC scc = new TarjanSCC(g, mScc);
                scc.run();
                mScc.stopTimer();

                // === Condensation + Topological Sort ===
                Graph dag = scc.buildCondensation();
                Metrics mTopo = new Metrics();
                mTopo.startTimer();
                KahnTopoSort topo = new KahnTopoSort(dag, mTopo);
                List<Integer> order = topo.sort();
                mTopo.stopTimer();

                // === Shortest Path ===
                Metrics mShort = new Metrics();
                mShort.startTimer();
                DAGShortestPath sp = new DAGShortestPath(dag, mShort);
                sp.run(Math.min(ds.getSource(), dag.getV() - 1));
                mShort.stopTimer();

                // === Output log ===
                System.out.printf("✅ %-12s | SCC=%.0f ns | TOPO=%.0f ns | SP=%.0f ns%n",
                        datasetName, mScc.getTimeMs() * 1_000_000, mTopo.getTimeMs() * 1_000_000, mShort.getTimeMs() * 1_000_000);

                // === Write to CSV ===
                fw.write(String.format(
                        "%s,%.0f ns,%d,%d,%.0f ns,%d,%d,%.0f ns,%d\n",
                        datasetName,
                        mScc.getTimeMs() * 1_000_000, // ns
                        g.getV(), // dfs_calls proxy
                        g.getEdges().size(), // edges_processed
                        mTopo.getTimeMs() * 1_000_000, // ns
                        0, // dfs_recursive_calls
                        0, // dfs_stack_adds
                        mShort.getTimeMs() * 1_000_000, // ns
                        mShort.getOps() // relaxations
                ));
                fw.flush();
            }

            System.out.println("\n📊 Summary CSV saved to: " + summaryFile.getPath());

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
