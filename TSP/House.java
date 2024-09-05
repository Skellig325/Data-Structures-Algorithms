public class House {
    private String address;
    private int id;
    private double[] distances;

    public House(String address, int id, double[] distances) {
        this.address = address;
        this.id = id;
        this.distances = distances.clone(); // Clone the array to prevent external modifications
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public double[] getDistances() {
        return distances.clone(); // Return a clone to prevent external modifications
    }

    public double getDistanceToHouse(int houseId) {
        return distances[houseId];
    }

    @Override
    public String toString() {
        return "House ID: " + id + ", Address: " + address; // replace with whatever you want to print
    }
}