public abstract class Room {

    private String roomID;
    private int capacity;
    private boolean isAvailable;
    private double pricePerHour;-

    // Constructor
    public Room(String roomID, int capacity, double pricePerHour) {
        this.roomID = roomID;
        this.capacity = capacity;
        this.isAvailable = true;
        this.pricePerHour = pricePerHour;
    }

    // --- Getters & Setters ---
    public String getRoomID() { return roomID; }
    public int getCapacity() { return capacity; }
    public boolean isAvailable() { return isAvailable; }
    public double getPricePerHour() { return pricePerHour; }

    public void setAvailable(boolean status) { this.isAvailable = status; }
    public void setPricePerHour(double p) { this.pricePerHour = p; }

    // Check availability for a given time duration
    public boolean checkAvailability(String date, String time) {
        return isAvailable;
    }

    // Calculate total price for given hours
    public double calculatePrice(double hours) {
        return pricePerHour * hours;
    }

    // Abstract method — must be implemented by subclasses (Abstraction)
    public abstract String getRoomDetails();

    @Override
    public String toString() {
        return String.format("Room ID: %-8s | Capacity: %-4d | Available: %-5s | Price/hr: RM%.2f",
                roomID, capacity, isAvailable ? "Yes" : "No", pricePerHour);
    }
}