package org.example.io;

import org.example.model.Graph;
import org.example.scc.TarjanSCC;
import org.example.util.Metrics;

import java.io.FileWriter;
import java.io.IOException;

public class SCCCsvWriter {
    public static void write(String filename, Graph g, TarjanSCC scc, Metrics m) {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("vertices,edges,scc_count,ops,time_ms\n");
            fw.write(String.format("%d,%d,%d,%d,%.3f\n",
                    g.getV(),
                    g.getEdges().size(),
                    scc.getComponents().size(),
                    m.getOps(),
                    m.getTimeMs()
            ));
        } catch (IOException ex) {
            throw new RuntimeException("CSV write error: " + filename, ex);
        }
    }
}
