import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Booking implements Bookable {

    // Encapsulated fields
    private String bookingID;
    private User user;          
    private Room room;          
    private String date;
    private String startTime;
    private String endTime;
    private double totalHours;
    private double totalPrice;
    private boolean isActive;
    private LocalDateTime checkInDate;

    private static int bookingCounter = 1;

    public Booking(User user, Room room, String date, String startTime, String endTime) {
        this.bookingID = String.format("BK%03d", bookingCounter++);
        this.user = user;
        this.room = room;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalHours = calculateDuration(startTime, endTime);
        this.totalPrice = room.calculatePrice(totalHours);
        this.isActive = true;
        this.checkInDate = LocalDateTime.now();

        room.setAvailable(false);
    }

  
    private double calculateDuration(String start, String end) {
        String[] s = start.split(":");
        String[] e = end.split(":");
        int startMins = Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
        int endMins   = Integer.parseInt(e[0]) * 60 + Integer.parseInt(e[1]);
        return (endMins - startMins) / 60.0;
    }

    // --- Getters ---
    public String getBookingID() { return bookingID; }
    public User getUser()        { return user; }
    public Room getRoom()        { return room; }
    public boolean isActive()    { return isActive; }
    public double getTotalPrice(){ return totalPrice; }

    public void setCheckInDate(java.util.Date d) {
        // Accepts java.util.Date for compatibility
        this.checkInDate = d.toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime();
    }

    // --- Bookable Interface implementations ---

    @Override
    public boolean makeBooking(String date, String startTime, String endTime) {
        return isActive;
    }

    @Override
    public boolean cancelBooking(String bookingID) {
        if (this.bookingID.equals(bookingID) && isActive) {
            isActive = false;
            room.setAvailable(true); // Free up the room
            return true;
        }
        return false;
    }

    @Override
    public String getBookingSummary() {
        return getSummary();
    }

    public double calculateTotal() {
        return totalPrice;
    }

    public String getSummary() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format(
            "\n========== BOOKING SUMMARY ==========\n" +
            "  Booking ID  : %s\n" +
            "  Status      : %s\n" +
            "  User        : %s\n" +
            "  Room ID     : %s\n" +
            "  Date        : %s\n" +
            "  Time        : %s - %s (%.1f hrs)\n" +
            "  Total Price : RM%.2f\n" +
            "  Booked At   : %s\n" +
            "======================================",
            bookingID,
            isActive ? "ACTIVE" : "CANCELLED",
            user.getName(),
            room.getRoomID(),
            date, startTime, endTime, totalHours,
            totalPrice,
            checkInDate.format(fmt)
        );
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | %-8s | %s  %s-%s | RM%.2f | %s",
                bookingID, user.getName(), room.getRoomID(),
                date, startTime, endTime, totalPrice,
                isActive ? "ACTIVE" : "CANCELLED");
    }
}