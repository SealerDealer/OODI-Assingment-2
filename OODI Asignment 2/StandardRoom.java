
public class StandardRoom extends Room {

    private String bedType;
    private boolean hasWifi;

    public StandardRoom(String roomID, int capacity, double pricePerHour,
                        String bedType, boolean hasWifi) {
        super(roomID, capacity, pricePerHour); 
        this.bedType = bedType;
        this.hasWifi = hasWifi;
    }

    // Getters & Setters
    public String getBedType() { return bedType; }
    public void setBedType(String b) { this.bedType = b; }
    public boolean isWifiAvail() { return hasWifi; }

    // Polymorphism: overrides abstract method from Room
    @Override
    public String getRoomDetails() {
        return String.format(
            "[ STANDARD ROOM ]\n" +
            "  Room ID     : %s\n" +
            "  Capacity    : %d persons\n" +
            "  Bed Type    : %s\n" +
            "  WiFi        : %s\n" +
            "  Price/hr    : RM%.2f\n" +
            "  Available   : %s",
            getRoomID(), getCapacity(), bedType,
            hasWifi ? "Yes" : "No",
            getPricePerHour(),
            isAvailable() ? "Yes" : "No"
        );
    }
}