package org.example;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Graph graph = readGraphFromFile("logistica.txt");

        if (graph != null) {
            int[][] distances = graph.floydWarshall();
            graph.printShortestPaths(distances);
            List<String> centerVertices = graph.calculateCenter();
            System.out.println("Center vertices: " + centerVertices);
        }
    }

    private static Graph readGraphFromFile(String fileName) {
        Graph graph = null;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 6) {
                    System.out.println("Invalid line format: " + line);
                    return null;
                }

                String source = parts[0];
                String destination = parts[1];
                int normalTime = Integer.parseInt(parts[2]);
                int rainyTime = Integer.parseInt(parts[3]);
                int snowyTime = Integer.parseInt(parts[4]);
                int stormyTime = Integer.parseInt(parts[5]);
                int weight = normalTime; // You can choose a different time value if desired

                if (graph == null) {
                    graph = new Graph(0);
                }

                graph.addEdge(source, destination, weight);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return graph;
    }
    private static String getKeyByValue(Map<String, Integer> map, int value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }
}