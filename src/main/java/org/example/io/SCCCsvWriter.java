package org.example.io;

import org.example.util.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SCCCsvWriter {

    // ⚡ Метод, создающий CSV-файл с нужными колонками
    public static void writeSummary(String filename, String datasetName,
                                    Metrics mScc, Metrics mTopo, Metrics mSp) {
        File out = new File(filename);
        File parent = out.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        boolean writeHeader = !out.exists();

        try (FileWriter fw = new FileWriter(out, true)) {
            if (writeHeader) {
                fw.write("dataset_name,scc_duration_ns,dfs_calls,edges_processed,topo_duration_ns,dfs_recursive_calls,dfs_stack_adds,shortest_path_ns,edge_relaxations\n");
            }

            // У нас нет отдельных полей, поэтому подставляем 0 для отсутствующих
            fw.write(String.format("%s,%.0f ns,%d,%d,%.0f ns,%d,%d,%.0f ns,%d\n",
                    datasetName,
                    mScc.getTimeMs() * 1_000_000,  // перевод ms → ns
                    0, 0,
                    mTopo.getTimeMs() * 1_000_000,
                    0, 0,
                    mSp.getTimeMs() * 1_000_000,
                    0
            ));
            fw.flush();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write summary CSV: " + filename, ex);
        }
    }
}
