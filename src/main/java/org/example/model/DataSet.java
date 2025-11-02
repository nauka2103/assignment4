package org.example.model;


public class DataSet {
    private final Graph graph;
    private final int source;
    private final String weightModel;
    private final String resourceName;

    public DataSet(Graph graph, int source, String weightModel, String resourceName) {
        this.graph = graph;
        this.source = source;
        this.weightModel = weightModel;
        this.resourceName = resourceName;
    }

    public Graph getGraph() { return graph; }
    public int getSource() { return source; }
    public String getW() { return weightModel; }
    public String getResourceName() { return resourceName; }
}
