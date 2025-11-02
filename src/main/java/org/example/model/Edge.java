package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO edge matching JSON keys u,v,w
 */
public class Edge {
    private int u;
    private int v;
    private double w;

    private Edge() {}

    @JsonCreator
    public Edge(@JsonProperty("u") int u,
                @JsonProperty("v") int v,
                @JsonProperty("w") double w) {
        this.u = u; this.v = v; this.w = w;
    }

    public static Edge createEdge() {
        return new Edge();
    }

    public int getU() { return u; }
    public void setU(int u) { this.u = u; }

    public int getV() { return v; }
    public void setV(int v) { this.v = v; }

    public double getW() { return w; }
    public void setW(double w) { this.w = w; }

    @Override
    public String toString() {
        return "(" + u + "->" + v + ",w=" + w + ")";
    }
}
