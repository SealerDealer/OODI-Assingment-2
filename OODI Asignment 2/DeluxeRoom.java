public class DeluxeRoom extends Room {

    private boolean hasJacuzzi;
    private int floorLevel;

    public DeluxeRoom(String roomID, int capacity, double pricePerHour,
                      boolean hasJacuzzi, int floorLevel) {
        super(roomID, capacity, pricePerHour); // Call parent constructor
        this.hasJacuzzi = hasJacuzzi;
        this.floorLevel = floorLevel;
    }

    // Getters & Setters
    public boolean hasJacuzzi() { return hasJacuzzi; }
    public int getFloorLevel() { return floorLevel; }
    public void setFloorLevel(int f) { this.floorLevel = f; }

    // Polymorphism: overrides abstract method from Room
    @Override
    public String getRoomDetails() {
        return String.format(
            "[ DELUXE ROOM ]\n" +
            "  Room ID     : %s\n" +
            "  Capacity    : %d persons\n" +
            "  Floor Level : %d\n" +
            "  Jacuzzi     : %s\n" +
            "  Price/hr    : RM%.2f\n" +
            "  Available   : %s",
            getRoomID(), getCapacity(), floorLevel,
            hasJacuzzi ? "Yes" : "No",
            getPricePerHour(),
            isAvailable() ? "Yes" : "No"
        );
    }
}