/**
 * Interface for bookable entities.
 * Demonstrates: Interface (Abstraction)
 */
public interface Bookable {
    boolean makeBooking(String date, String startTime, String endTime);
    boolean cancelBooking(String bookingID);
    String getBookingSummary();
}