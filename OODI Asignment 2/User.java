
public class User {

    // Encapsulated fields
    private String userID;
    private String name;
    private String email;
    private String phone;

    public User(String userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = "";
    }

    // Getters & Setters (Encapsulation)
    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void updateContact(String phone) {
        this.phone = phone;
    }

    public String getDetails() {
        return String.format("User ID: %s | Name: %s | Email: %s | Phone: %s",
                userID, name, email, phone.isEmpty() ? "N/A" : phone);
    }

    @Override
    public String toString() {
        return name + " (" + userID + ")";
    }
}