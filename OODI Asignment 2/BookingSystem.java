
public class BookingSystem {

    private String systemID;

    private Room[] availableRooms;
    private Booking[] bookings;
    private int roomCount;
    private int bookingCount;

    private static final int MAX_ROOMS    = 20;
    private static final int MAX_BOOKINGS = 100;
    private static final int MAX_BOOKING_HOURS = 4; 

    public BookingSystem(String systemID) {
        this.systemID = systemID;
        this.availableRooms = new Room[MAX_ROOMS];
        this.bookings       = new Booking[MAX_BOOKINGS];
        this.roomCount      = 0;
        this.bookingCount   = 0;
    }

    // --- Room Management ---

    public void addRoom(Room room) {
        if (roomCount < MAX_ROOMS) {
            availableRooms[roomCount++] = room;
        } else {
            System.out.println("  [!] Room list is full.");
        }
    }

    public Room[] getRoomList() {
        Room[] result = new Room[roomCount];
        for (int i = 0; i < roomCount; i++) result[i] = availableRooms[i];
        return result;
    }

    // Polymorphism in action: iterates Room array, calls getRoomDetails() on each
    // (actual runtime type may be StandardRoom or DeluxeRoom)
    public void displayAvailableRooms() {
        System.out.println("\n  ============ AVAILABLE ROOMS ============");
        boolean found = false;
        for (int i = 0; i < roomCount; i++) {
            if (availableRooms[i].isAvailable()) {
                System.out.println(availableRooms[i]); // calls overridden toString
                found = true;
            }
        }
        if (!found) System.out.println("  No rooms currently available.");
        System.out.println("  =========================================");
    }

    public void displayAllRooms() {
        System.out.println("\n  ================ ALL ROOMS ===============");
        for (int i = 0; i < roomCount; i++) {
            System.out.println("  " + (i + 1) + ". " + availableRooms[i]);
        }
        System.out.println("  ==========================================");
    }

    // FR-01: Validate room availability by iterating through Room array
    public Room[] searchRooms(String date, String time, int requiredCapacity) {
        Room[] results = new Room[roomCount];
        int count = 0;
        for (int i = 0; i < roomCount; i++) {
            Room r = availableRooms[i];
            if (r.checkAvailability(date, time) && r.getCapacity() >= requiredCapacity) {
                results[count++] = r;
            }
        }
        Room[] trimmed = new Room[count];
        for (int i = 0; i < count; i++) trimmed[i] = results[i];
        return trimmed;
    }

    public boolean checkAvailability(String date, String time) {
        for (int i = 0; i < roomCount; i++) {
            if (availableRooms[i].checkAvailability(date, time)) return true;
        }
        return false;
    }

    public Room findRoomByID(String roomID) {
        for (int i = 0; i < roomCount; i++) {
            if (availableRooms[i].getRoomID().equalsIgnoreCase(roomID)) {
                return availableRooms[i];
            }
        }
        return null;
    }

    // --- Booking Management ---

    /**
     * Creates a new booking. FR-02: validates max 4-hour duration.
     * FR-03: instantiates a new Booking object.
     * FR-04: updates room isAvailable to false.
     */
    public Booking createBooking(User user, Room room, String date,
                                 String startTime, String endTime) {
        // FR-02: Validate duration <= 4 hours
        double hours = parseHours(startTime, endTime);
        if (hours <= 0) {
            displayError("Invalid time range. End time must be after start time.");
            return null;
        }
        if (hours > MAX_BOOKING_HOURS) {
            displayError("Booking duration cannot exceed " + MAX_BOOKING_HOURS + " hours.");
            return null;
        }
        if (!room.isAvailable()) {
            displayError("Room " + room.getRoomID() + " is no longer available.");
            return null;
        }
        if (bookingCount >= MAX_BOOKINGS) {
            displayError("Booking storage is full.");
            return null;
        }

        // FR-03: Instantiate new Booking object
        // FR-04: Room status updated inside Booking constructor
        Booking b = new Booking(user, room, date, startTime, endTime);
        bookings[bookingCount++] = b;
        return b;
    }

    public boolean cancelBooking(String bookingID) {
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].getBookingID().equalsIgnoreCase(bookingID)) {
                return bookings[i].cancelBooking(bookingID);
            }
        }
        displayError("Booking ID not found: " + bookingID);
        return false;
    }

    public void displayAllBookings() {
        System.out.println("\n  ============== ALL BOOKINGS ==============");
        System.out.printf("  %-8s | %-20s | %-8s | %-20s | %-8s | %s%n",
                "ID", "User", "Room", "Date & Time", "Price", "Status");
        System.out.println("  " + "-".repeat(85));
        if (bookingCount == 0) {
            System.out.println("  No bookings recorded.");
        } else {
            for (int i = 0; i < bookingCount; i++) {
                System.out.println("  " + bookings[i]);
            }
        }
        System.out.println("  ==========================================");
    }

    public void generateUsageReport() {
        int active = 0, cancelled = 0;
        double totalRevenue = 0;
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].isActive()) {
                active++;
                totalRevenue += bookings[i].getTotalPrice();
            } else {
                cancelled++;
            }
        }
        System.out.println("\n  ========== USAGE REPORT ==========");
        System.out.println("  System ID       : " + systemID);
        System.out.println("  Total Rooms     : " + roomCount);
        System.out.println("  Total Bookings  : " + bookingCount);
        System.out.println("  Active Bookings : " + active);
        System.out.println("  Cancelled       : " + cancelled);
        System.out.printf ("  Total Revenue   : RM%.2f%n", totalRevenue);
        System.out.println("  ==================================");
    }

    public void displayError(String message) {
        System.out.println("  [ERROR] " + message);
    }

    // Helper: parse hours from HH:mm strings
    private double parseHours(String start, String end) {
        try {
            String[] s = start.split(":");
            String[] e = end.split(":");
            int sm = Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
            int em = Integer.parseInt(e[0]) * 60 + Integer.parseInt(e[1]);
            return (em - sm) / 60.0;
        } catch (Exception ex) {
            return -1;
        }
    }
}