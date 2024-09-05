import java.io.*;
import java.util.List;
import java.util.Scanner;
// Make sure to import the DeliveryRouteFinder and House classes
// import your.package.DeliveryRouteFinder;
// import your.package.House;

public class LoadFile {
    private static String[] addresses;
    private static double[][] distances;

    public static void main(String[] args) {
        File file = new File("deliveries.csv");
        addresses = new String[100];
        distances = new double[100][100];

        try {
            Scanner scan = new Scanner(file);
            for (int i = 0; i < 100; i++) {
                String line = scan.nextLine();
                String[] parts = line.split(","); // Split the line by commas
                addresses[i] = parts[0]; // get the address
                for (int j = 0; j < 100; j++) { // get the distances
                    distances[i][j] = Integer.parseInt(parts[j + 1].trim()); // Convert to kilometers
                }
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        double shortestTotalDistance = Double.MAX_VALUE;
        int bestStartingPoint = -1;

        for (int i = 0; i < 100; i++) {
            double totalDistance = calculateTotalDistance(i);
            if (totalDistance < shortestTotalDistance) {
                shortestTotalDistance = totalDistance;
                bestStartingPoint = i;
            }
        }

        System.out.println("Best starting point is " + addresses[bestStartingPoint] + " with a total distance of "
                + shortestTotalDistance + " m");

        // New code
        DeliveryRouteFinder finder = new DeliveryRouteFinder(distances);
        List<Integer> shortestPath = finder.findRoute(0); // startHouse is set to 0 as an example

        for (Integer houseIndex : shortestPath) {
            System.out.println(addresses[houseIndex]);
        }
    }

    private static double calculateTotalDistance(int startHouse) {
        boolean[] visited = new boolean[100];
        double totalDistance = 0;
        int currentHouse = startHouse;

        while (true) {
            visited[currentHouse] = true;
            int nextHouse = -1;
            double shortestDistance = Double.MAX_VALUE;

            for (int i = 0; i < 100; i++) {
                if (!visited[i] && distances[currentHouse][i] < shortestDistance) {
                    nextHouse = i;
                    shortestDistance = distances[currentHouse][i];
                }
            }

            if (nextHouse == -1) {
                break;
            }

            totalDistance += shortestDistance;
            currentHouse = nextHouse;
        }

        totalDistance += distances[currentHouse][startHouse]; // Add the distance back to the start house
        return totalDistance;
    }

    public static double[][] getDistances() {
        return distances;
    }
}