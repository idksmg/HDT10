package org.example;

import java.util.*;

public class Graph {
    private int[][] adjMatrix;
    private static final int INFINITY = Integer.MAX_VALUE;
    private Map<String, Integer> vertexMap;
    private int[][] adjacencyMatrix;
    private int numVertices;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.adjMatrix = new int[numVertices][numVertices];
        this.vertexMap = new HashMap<>();
    }

    public void addEdge(String source, String destination, int weight) {
        int sourceIndex = vertexMap.getOrDefault(source, -1);
        int destinationIndex = vertexMap.getOrDefault(destination, -1);

        if (sourceIndex == -1) {
            sourceIndex = vertexMap.size();
            vertexMap.put(source, sourceIndex);
            numVertices++; // Increment the number of vertices
            resizeAdjacencyMatrix(); // Resize the adjacency matrix
        }
        if (destinationIndex == -1) {
            destinationIndex = vertexMap.size();
            vertexMap.put(destination, destinationIndex);
            numVertices++; // Increment the number of vertices
            resizeAdjacencyMatrix(); // Resize the adjacency matrix
        }

        adjMatrix[sourceIndex][destinationIndex] = weight;
    }

    public void removeEdge(String source, String destination) {
        int sourceIndex = vertexMap.get(source);
        int destIndex = vertexMap.get(destination);
        adjacencyMatrix[sourceIndex][destIndex] = 0;
    }

    public void printShortestPaths(int[][] distances) {
        System.out.println("Shortest Paths:");

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (distances[i][j] == INFINITY) {
                    System.out.println(getKeyByValue(vertexMap, i) + " -> " + getKeyByValue(vertexMap, j) + ": No path");
                } else {
                    System.out.println(getKeyByValue(vertexMap, i) + " -> " + getKeyByValue(vertexMap, j) + ": " + distances[i][j]);
                }
            }
        }
    }



    private void printShortestPaths(int[][] distance, int[][] next) {
        System.out.println("Shortest Paths:");

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j) {
                    String source = getKeyByValue(vertexMap, i);
                    String destination = getKeyByValue(vertexMap, j);
                    System.out.print("Shortest path from " + source + " to " + destination + ": ");
                    System.out.print(source + " -> ");
                    int intermediate = next[i][j];
                    while (intermediate != j) {
                        System.out.print(getKeyByValue(vertexMap, intermediate) + " -> ");
                        intermediate = next[intermediate][j];
                    }
                    System.out.println(destination + " (weight: " + distance[i][j] + ")");
                }
            }
        }
    }

    public int getCenter() {
        int[] eccentricity = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            int maxDistance = 0;
            for (int j = 0; j < numVertices; j++) {
                if (adjacencyMatrix[i][j] != 0 && adjacencyMatrix[i][j] > maxDistance) {
                    maxDistance = adjacencyMatrix[i][j];
                }
            }
            eccentricity[i] = maxDistance;
        }

        int center = 0;
        int minEccentricity = eccentricity[0];

        for (int i = 1; i < numVertices; i++) {
            if (eccentricity[i] < minEccentricity) {
                minEccentricity = eccentricity[i];
                center = i;
            }
        }

        return center;
    }

    public Map<String, Integer> getVertexMap() {
        return vertexMap;
    }

    public void setVertexMap(Map<String, Integer> vertexMap) {
        this.vertexMap = vertexMap;
    }

    private String getKeyByValue(Map<String, Integer> map, int value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }

    public int[][] floydWarshall() {
        int[][] distances = new int[numVertices][numVertices];

        // Initialize distances with the weights of the existing edges
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    distances[i][j] = 0; // Distance from a node to itself is 0
                } else if (adjMatrix[i][j] != 0) {
                    distances[i][j] = adjMatrix[i][j]; // Distance between connected nodes is the weight of the edge
                } else {
                    distances[i][j] = INFINITY; // Initialize all other distances as infinity
                }
            }
        }

        // Perform the Floyd's algorithm
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distances[i][k] != INFINITY && distances[k][j] != INFINITY
                            && distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j]; // Update the shortest path distance
                    }
                }
            }
        }

        return distances;
    }
    private void resizeAdjacencyMatrix() {
        int[][] newAdjMatrix = new int[numVertices][numVertices];

        for (int i = 0; i < adjMatrix.length; i++) {
            System.arraycopy(adjMatrix[i], 0, newAdjMatrix[i], 0, adjMatrix[i].length);
        }

        adjMatrix = newAdjMatrix;
    }
    public List<String> calculateCenter() {
        int[][] distances = floydWarshall();

        // Find the minimum eccentricity
        int minEccentricity = Integer.MAX_VALUE;
        for (int i = 0; i < numVertices; i++) {
            int maxDistance = 0;
            for (int j = 0; j < numVertices; j++) {
                if (distances[i][j] > maxDistance) {
                    maxDistance = distances[i][j];
                }
            }
            if (maxDistance < minEccentricity) {
                minEccentricity = maxDistance;
            }
        }

        // Find the vertices with the minimum eccentricity
        List<String> centerVertices = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : vertexMap.entrySet()) {
            String vertex = entry.getKey();
            int index = entry.getValue();
            int maxDistance = 0;
            for (int i = 0; i < numVertices; i++) {
                if (distances[index][i] > maxDistance) {
                    maxDistance = distances[index][i];
                }
            }
            if (maxDistance == minEccentricity) {
                centerVertices.add(vertex);
            }
        }

        return centerVertices;
    }
}

