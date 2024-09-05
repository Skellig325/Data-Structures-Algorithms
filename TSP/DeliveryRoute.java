import java.util.*;

class DeliveryRoute {
    private List<House> houses;
    private double totalDistance;

    public DeliveryRoute() {
        this.houses = new ArrayList<>();
        this.totalDistance = 0;
    }

    // Method to add a house to the delivery route
    public void addHouse(House house) {
        // If there is at least one house in the list
        if (!this.houses.isEmpty()) {
            // Get the last house in the list
            House lastHouse = this.houses.get(this.houses.size() - 1);
            // Add the distance from the last house to the new house to the total distance
            this.totalDistance += lastHouse.getDistanceToHouse(house.getId());
        }
        // Add the house to the list
        this.houses.add(house);
    }

    public double getTotalDistance() {
        return this.totalDistance;
    }

    public void printRoute() {
        for (House house : this.houses) {
            System.out.println(house);
        }
    }
}