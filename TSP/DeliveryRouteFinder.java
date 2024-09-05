import java.util.*;

public class DeliveryRouteFinder {
    // 2D array to store the distances between each pair of houses
    private double[][] distances;

    // Constructor that takes an adjacency matrix of distances
    public DeliveryRouteFinder(double[][] adjacencyMatrix) {
        int numHouses = adjacencyMatrix.length;
        this.distances = new double[numHouses][numHouses];

        // For each house, calculate the shortest distances to all other houses
        for (int i = 0; i < numHouses; i++) {
            this.distances[i] = dijkstra(adjacencyMatrix, i);
        }
    }

    // Dijkstra's algorithm to find the shortest distances from a source house to
    // all other houses
    private double[] dijkstra(double[][] adjacencyMatrix, int sourceHouse) {
        int numHouses = adjacencyMatrix.length;
        double[] distances = new double[numHouses];
        boolean[] visited = new boolean[numHouses];

        // Initialize distances to all houses as infinite and distance to source house
        // as 0
        Arrays.fill(distances, Double.MAX_VALUE);
        distances[sourceHouse] = 0;

        // Find shortest path for all houses
        for (int i = 0; i < numHouses; i++) {
            // Pick the minimum distance house from the set of houses not yet processed
            int nearestHouse = findNearestHouse(distances, visited);
            visited[nearestHouse] = true;

            // Update distances of the adjacent houses of the picked house
            for (int j = 0; j < numHouses; j++) {
                if (!visited[j] && adjacencyMatrix[nearestHouse][j] != 0 &&
                        distances[nearestHouse] != Double.MAX_VALUE &&
                        distances[nearestHouse] + adjacencyMatrix[nearestHouse][j] < distances[j]) {
                    distances[j] = distances[nearestHouse] + adjacencyMatrix[nearestHouse][j];
                }
            }
        }

        return distances;
    }

    // A utility function to find the house with minimum distance value, from the
    // set of houses not yet visited
    private int findNearestHouse(double[] distances, boolean[] visited) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        List<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i] && distances[i] <= minDistance) {
                if (distances[i] < minDistance) {
                    minDistance = distances[i];
                    candidates.clear();
                }
                candidates.add(i);
            }
        }

        // Add some randomness: if there are multiple houses with the same minimum
        // distance,
        // select one of them at random
        if (!candidates.isEmpty()) {
            Random rand = new Random();
            minIndex = candidates.get(rand.nextInt(candidates.size()));
        }

        return minIndex;
    }

    // Function to find the shortest delivery route starting from a given house
    public List<Integer> findRoute(int startHouse) {
        List<Integer> route = new ArrayList<>();
        boolean[] visited = new boolean[distances.length];

        int currentHouse = startHouse;
        visited[currentHouse] = true;
        route.add(currentHouse);

        // Keep adding the nearest unvisited house to the route until all houses have
        // been visited
        while (route.size() < distances.length) {
            int nextHouse = findNearestUnvisitedHouse(currentHouse, visited);
            visited[nextHouse] = true;
            route.add(nextHouse);
            currentHouse = nextHouse;
        }

        // Add the start house at the end to complete the route
        route.add(startHouse);

        // Optimize the route using 2-opt algorithm
        twoOpt(route);

        return route;
    }

    // A utility function to find the nearest unvisited house from a given house
    private int findNearestUnvisitedHouse(int house, boolean[] visited) {
        int nearestHouse = -1;
        double nearestDistance = Double.MAX_VALUE;

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i] && distances[house][i] < nearestDistance) {
                nearestHouse = i;
                nearestDistance = distances[house][i];
            }
        }

        return nearestHouse;
    }

    // 2-opt algorithm to optimize a route
    private void twoOpt(List<Integer> route) {
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < route.size() - 2; i++) {
                for (int j = i + 2; j < route.size() - 1; j++) {
                    // Calculate the current distance and the new distance
                    double oldDistance = distances[route.get(i)][route.get(i + 1)]
                            + distances[route.get(j)][route.get(j + 1)];
                    double newDistance = distances[route.get(i)][route.get(j)]
                            + distances[route.get(i + 1)][route.get(j + 1)];

                    // If the new distance is shorter, swap the edges
                    if (newDistance < oldDistance) {
                        Collections.reverse(route.subList(i + 1, j + 1));
                        improved = true;
                    }
                }
            }
        }
    }

    // Static factory method to create a DeliveryRouteFinder instance from a
    // LoadFile instance
    public static DeliveryRouteFinder fromLoadFile(LoadFile loadFile) {
        return new DeliveryRouteFinder(LoadFile.getDistances());
    }
}