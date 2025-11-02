package org.example.util;


public class Metrics {
    private long ops = 0;
    private long t0 = 0;
    private double timeMs = 0;

    public void incOps() { ops++; }
    public long getOps() { return ops; }

    public void startTimer() { t0 = System.nanoTime(); }
    public void stopTimer() {
        if (t0 != 0) timeMs = (System.nanoTime() - t0) / 1_000_000.0;
    }
    public double getTimeMs() { return timeMs; }

    public void reset() { ops = 0; t0 = 0; timeMs = 0; }
}
