import java.util.Scanner;

/**
 * Main CLI entry point for the Meeting Room Booking System.
 * Course: DRC1213 – Object-Oriented Design & Implementation
 * Phase 2: CLI Implementation
 *
 * OOP Concepts demonstrated:
 *   - Encapsulation  : private fields + getters/setters in all classes
 *   - Inheritance    : StandardRoom, DeluxeRoom extend Room
 *   - Polymorphism   : getRoomDetails() overridden in subclasses
 *   - Abstraction    : Room is abstract; Bookable is an interface
 *   - Composition    : Booking contains User + Room
 *   - Aggregation    : BookingSystem holds array of Rooms
 *   - Array Storage  : Room[] and Booking[] as in-memory storage (CO3)
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static BookingSystem system = new BookingSystem("SYS-001");
    static User currentUser = null;

    public static void main(String[] args) {
        seedData();
        printBanner();
        loginMenu();
    }

    // -------------------------------------------------------
    // SEED DATA (pre-loaded rooms)
    // -------------------------------------------------------
    static void seedData() {
        system.addRoom(new StandardRoom("R001", 10, 50.0,  "Conference",  true));
        system.addRoom(new StandardRoom("R002", 6,  30.0,  "Discussion",  true));
        system.addRoom(new DeluxeRoom  ("R003", 20, 120.0, true,  5));
        system.addRoom(new DeluxeRoom  ("R004", 15, 90.0,  false, 3));
        system.addRoom(new StandardRoom("R005", 4,  20.0,  "Huddle",      false));
    }

    // BANNER
    static void printBanner() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     MEETING ROOM BOOKING SYSTEM          ║");
        System.out.println("║     Universiti Malaysia Pahang           ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // LOGIN MENU
    static void loginMenu() {
        while (true) {
            System.out.println("\n  ---- LOGIN / REGISTER ----");
            System.out.println("  1. Login as User");
            System.out.println("  2. Login as Administrator");
            System.out.println("  3. Register New User");
            System.out.println("  0. Exit");
            System.out.print("  Choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": userLogin(); break;
                case "2": adminLogin(); break;
                case "3": registerUser(); break;
                case "0":
                    System.out.println("\n  Thank you for using the system. Goodbye!");
                    System.exit(0);
                    break;
                default: System.out.println("  Invalid choice. Try again.");
            }
        }
    }

    static void userLogin() {
        System.out.print("  Enter your name: ");
        String name = sc.nextLine().trim();
        System.out.print("  Enter your User ID: ");
        String uid = sc.nextLine().trim();
        currentUser = new User(uid, name, uid + "@ump.edu.my");
        System.out.println("  Welcome, " + name + "!");
        userMenu();
    }

    static void adminLogin() {
        System.out.print("  Admin password: ");
        String pw = sc.nextLine().trim();
        if (pw.equals("admin123")) {
            System.out.println("  Admin access granted.");
            adminMenu();
        } else {
            System.out.println("  Incorrect password.");
        }
    }

    static void registerUser() {
        System.out.print("  Enter name      : ");
        String name = sc.nextLine().trim();
        System.out.print("  Enter User ID   : ");
        String uid = sc.nextLine().trim();
        System.out.print("  Enter email     : ");
        String email = sc.nextLine().trim();
        currentUser = new User(uid, name, email);
        System.out.println("  Registration successful! Welcome, " + name + "!");
        userMenu();
    }

    // -------------------------------------------------------
    // USER MENU
    // -------------------------------------------------------
    static void userMenu() {
        while (true) {
            System.out.println("\n  ========== USER MENU ==========");
            System.out.println("  1. View Available Rooms");
            System.out.println("  2. Make a Booking");
            System.out.println("  3. Cancel a Booking");
            System.out.println("  4. View Room Details");
            System.out.println("  0. Logout");
            System.out.print("  Choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": viewAvailableRooms(); break;
                case "2": makeBooking();        break;
                case "3": cancelBooking();      break;
                case "4": viewRoomDetails();    break;
                case "0":
                    System.out.println("  Logged out.");
                    currentUser = null;
                    return;
                default: System.out.println("  Invalid choice.");
            }
        }
    }

    // -------------------------------------------------------
    // ADMIN MENU
    // -------------------------------------------------------
    static void adminMenu() {
        while (true) {
            System.out.println("\n  ======== ADMIN MENU =========");
            System.out.println("  1. View All Rooms");
            System.out.println("  2. View All Bookings");
            System.out.println("  3. Generate Usage Report");
            System.out.println("  0. Logout");
            System.out.print("  Choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": system.displayAllRooms();    break;
                case "2": system.displayAllBookings(); break;
                case "3": system.generateUsageReport(); break;
                case "0":
                    System.out.println("  Admin logged out.");
                    return;
                default: System.out.println("  Invalid choice.");
            }
        }
    }

    // -------------------------------------------------------
    // USE CASES
    // -------------------------------------------------------

    // UC1: View Available Rooms
    static void viewAvailableRooms() {
        system.displayAvailableRooms();
    }

    // UC2: Make a Booking (matches Use Case Description in Phase 1)
    static void makeBooking() {
        System.out.println("\n  --- BOOK A ROOM ---");

        // Step 2: Prompt for booking details
        System.out.print("  Enter date (DD-MM-YYYY)  : ");
        String date = sc.nextLine().trim();
        System.out.print("  Enter start time (HH:MM) : ");
        String startTime = sc.nextLine().trim();
        System.out.print("  Enter end time   (HH:MM) : ");
        String endTime = sc.nextLine().trim();
        System.out.print("  Enter required capacity  : ");
        int capacity;
        try {
            capacity = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid capacity.");
            return;
        }

        // Step 4: System validates available rooms (FR-01)
        Room[] found = system.searchRooms(date, startTime, capacity);
        if (found.length == 0) {
            // Exception Flow [E1]
            System.out.println("\n  [!] Room Fully Booked - no rooms match your criteria.");
            System.out.println("  Please try a different time or date.");
            return;
        }

        // Step 5: Display available rooms
        System.out.println("\n  Available Rooms for " + date + " (" + startTime + "-" + endTime + "):");
        for (int i = 0; i < found.length; i++) {
            System.out.println("  " + (i + 1) + ". " + found[i]);
        }

        // Step 6: User selects room
        System.out.print("\n  Select room number: ");
        int sel;
        try {
            sel = Integer.parseInt(sc.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid selection.");
            return;
        }
        if (sel < 0 || sel >= found.length) {
            System.out.println("  [ERROR] Invalid room number.");
            return;
        }
        Room selectedRoom = found[sel];

        Booking booking = system.createBooking(currentUser, selectedRoom, date, startTime, endTime);
        if (booking == null) return;

        // Step 8: Display booking summary
        System.out.println(booking.getSummary());
        System.out.println("  Booking confirmed!");
    }

    // UC3: Cancel a Booking
    static void cancelBooking() {
        System.out.println("\n  --- CANCEL A BOOKING ---");
        System.out.print("  Enter Booking ID to cancel: ");
        String bid = sc.nextLine().trim();
        boolean success = system.cancelBooking(bid);
        if (success) {
            System.out.println("  Booking " + bid + " has been successfully cancelled.");
        }
    }

    // View detailed info for a specific room (Polymorphism demo)
    static void viewRoomDetails() {
        System.out.println("\n  --- ROOM DETAILS ---");
        system.displayAllRooms();
        System.out.print("  Enter Room ID: ");
        String rid = sc.nextLine().trim();
        Room r = system.findRoomByID(rid);
        if (r == null) {
            System.out.println("  Room not found.");
        } else {
            // Polymorphism: actual runtime type determines which getRoomDetails() is called
            System.out.println("\n" + r.getRoomDetails());
        }
    }
}